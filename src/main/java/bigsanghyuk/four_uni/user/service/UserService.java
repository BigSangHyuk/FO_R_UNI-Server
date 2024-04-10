package bigsanghyuk.four_uni.user.service;

import bigsanghyuk.four_uni.config.RedisUtil;
import bigsanghyuk.four_uni.config.jwt.JwtProvider;
import bigsanghyuk.four_uni.config.jwt.domain.Token;
import bigsanghyuk.four_uni.config.jwt.domain.TokenRepository;
import bigsanghyuk.four_uni.config.jwt.dto.TokenDto;
import bigsanghyuk.four_uni.config.mail.domain.SendMailInfo;
import bigsanghyuk.four_uni.config.mail.service.MailService;
import bigsanghyuk.four_uni.config.s3.service.S3Uploader;
import bigsanghyuk.four_uni.exception.jwt.TokenNotFoundException;
import bigsanghyuk.four_uni.exception.user.EmailDuplicateException;
import bigsanghyuk.four_uni.exception.user.PasswordMismatchException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.exception.user.WrongPasswordException;
import bigsanghyuk.four_uni.user.domain.*;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.dto.response.EditResponse;
import bigsanghyuk.four_uni.user.dto.response.LoginResponse;
import bigsanghyuk.four_uni.user.dto.response.SignResponse;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider  jwtProvider;
    private final TokenRepository tokenRepository;
    private final MailService mailService;
    private final RedisUtil redisUtil;
    private final S3Uploader s3Uploader;

    @PersistenceContext
    private final EntityManager em;

    private static final int EXPIRATION_IN_MINUTES = 43800;

    // 회원 가입
    public void register(SignUserInfo info) throws Exception {
        userRepository.findByEmail(info.getEmail())
                .ifPresent(user -> {
                    throw new EmailDuplicateException();
                });
        User user = userBuilder(info);
        try {
            userRepository.save(user);
        } catch (Exception e) {
            log.error("e.getMessage={}", e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
    }

    // 회원 정보 수정
    @Transactional
    public EditResponse edit(Long userId, EditUserInfo info) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (info.getImage() != null) {  // 새로운 이미지 등록 요청이 있고 이전 이미지가 s3에 업로드 되있었다면
            String oldImageUrl = user.getImage();
            s3Uploader.delete(oldImageUrl); // s3의 이미지 삭제
            user.updateImage(null);
        }
        editUser(user, info);
        User savedUser = userRepository.save(user);

        return editResponseBuilder(savedUser);
    }

    // 로그인
    public LoginResponse login(LoginUserInfo info) {
        User user = userRepository.findByEmail(info.getEmail())
                .orElseThrow(UserNotFoundException::new);
        if (!encoder.matches(info.getPassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }
        return loginResponseBuilder(user);
    }

    @Transactional
    public void logout(Long userId, LogoutUserInfo logoutUserInfo) throws JsonProcessingException {
        String accessToken = logoutUserInfo.getAccessToken();

        deleteRefreshToken(userId);
        setAccessTokenBlackList(accessToken);
    }

    @Transactional
    public void leave(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        deleteAll(user);
    }

    @Transactional
    protected void deleteAll(User user) {
        em.createQuery("DELETE FROM Report r WHERE r.user = :user").setParameter("user", user).executeUpdate();
        em.createQuery("DELETE FROM LikeComment lc WHERE lc.user = :user").setParameter("user", user).executeUpdate();
        em.createQuery("DELETE FROM Comment c WHERE c.user = :user").setParameter("user", user).executeUpdate();
        em.createQuery("DELETE FROM Scrapped s WHERE s.user = :user").setParameter("user", user).executeUpdate();
        userRepository.delete(user);
    }

    // 유저 상세 정보 조회
    public SignResponse getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return new SignResponse(user);
    }

    // Refresh Token 생성
    public String createRefreshToken(User user) {
        Token token = tokenRepository.save(Token.builder()
                .id(user.getId())
                .refreshToken(UUID.randomUUID().toString())
                .expiration(EXPIRATION_IN_MINUTES)
                .build()
        );
        return token.getRefreshToken();
    }

    // Refresh Token 검증
    public Token validRefreshToken(User user, String refreshToken) {
        Token token = tokenRepository.findById(user.getId()).orElseThrow(TokenNotFoundException::new);
        if (token.getRefreshToken() == null) {
            return null;
        } else {
            if (token.getExpiration() < 30) {   //30 minutes
                token.setExpiration(EXPIRATION_IN_MINUTES);
                tokenRepository.save(token);
            }
            return !token.getRefreshToken().equals(refreshToken) ? null : token;
        }
    }

    // Access Token 갱신
    public TokenDto refreshAccessToken(TokenDto tokenDto) throws Exception {
        Long userId = tokenRepository.findIdByRefreshToken(tokenDto.getRefreshToken())
                .orElseThrow(TokenNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Token token = validRefreshToken(user, tokenDto.getRefreshToken());
        if (token != null) {
            return tokenDtoBuilder(user, token);
        } else {
            throw new TokenNotFoundException();
        }
    }

    @Transactional
    protected void deleteRefreshToken(Long userId) {
        Token token = tokenRepository.findById(userId).orElseThrow(TokenNotFoundException::new);
        tokenRepository.delete(token);
    }

    private void setAccessTokenBlackList(String accessToken) throws JsonProcessingException {
        Long expiration = jwtProvider.getExpiration(accessToken);
        redisUtil.setBlackList(accessToken, "access_token", expiration);    // access token 유효기간 만큼 블랙리스트에 추가
    }

    // 임시 비밀번호 발급 후 전송
    @Transactional
    public Boolean setToTempPassword(SendMailInfo sendMailInfo) {
        String email = sendMailInfo.getEmail();
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        String encodedTempPw = encoder.encode(mailService.sendTempPwMail(sendMailInfo));
        user.updatePassword(encodedTempPw);
        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("오류가 발생했습니다.");
        }
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long userId, ChangePasswordInfo changePasswordInfo) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (encoder.matches(changePasswordInfo.getOldPassword(), user.getPassword())) { // 메일로 받은 임시 비밀번호랑 적용된 임시 비밀번호랑 같을 때 (발급시에 유저 비밀번호가 바뀜)
            user.updatePassword(encoder.encode(changePasswordInfo.getNewPassword()));
            userRepository.save(user);
        } else {
            throw new PasswordMismatchException();
        }
    }

    @Transactional
    protected void editUser(User user, EditUserInfo editUserInfo) {
        user.edit(
                editUserInfo.getDepartmentType() == null ? user.getDepartmentType() : editUserInfo.getDepartmentType(),
                editUserInfo.getNickName() == null ? user.getNickName() : editUserInfo.getNickName(),
                editUserInfo.getImage() == null ? user.getImage() : editUserInfo.getImage()
        );
    }

    private EditResponse editResponseBuilder(User user) {
        return EditResponse.builder()
                .id(user.getId())
                .departmentType(user.getDepartmentType())
                .nickName(user.getNickName())
                .image(user.getImage())
                .roles(user.getRoles())
                .build();
    }

    private User userBuilder(SignUserInfo info) {
        User user = User.builder()
                .email(info.getEmail())
                .password(encoder.encode(info.getPassword()))
                .departmentType(info.getDepartmentType())
                .nickName(info.getNickName())
                .image(info.getImage())
                .build();
        user.setRoles(Collections.singletonList(
                Authority.builder()
                        .name("ROLE_USER")
                        .build()));
        return user;
    }

    private LoginResponse loginResponseBuilder(User user) {
        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .departmentType(user.getDepartmentType())
                .nickName(user.getNickName())
                .image(user.getImage())
                .roles(user.getRoles())
                .token(TokenDto.builder()
                        .accessToken(jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles()))
                        .refreshToken(createRefreshToken(user))
                        .build())
                .build();
    }

    private TokenDto tokenDtoBuilder(User user, Token token) {
        return TokenDto.builder()
                .accessToken(jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles()))
                .refreshToken(token.getRefreshToken())
                .build();
    }
}
