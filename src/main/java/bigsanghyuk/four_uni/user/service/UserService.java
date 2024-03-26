package bigsanghyuk.four_uni.user.service;

import bigsanghyuk.four_uni.config.jwt.JwtProvider;
import bigsanghyuk.four_uni.config.jwt.domain.Token;
import bigsanghyuk.four_uni.config.jwt.domain.TokenRepository;
import bigsanghyuk.four_uni.config.jwt.dto.TokenDto;
import bigsanghyuk.four_uni.config.mail.domain.SendMailInfo;
import bigsanghyuk.four_uni.config.mail.service.MailService;
import bigsanghyuk.four_uni.exception.jwt.TokenNotFoundException;
import bigsanghyuk.four_uni.exception.user.EmailDuplicateException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.user.domain.ChangePasswordInfo;
import bigsanghyuk.four_uni.user.domain.EditUserInfo;
import bigsanghyuk.four_uni.user.domain.LoginUserInfo;
import bigsanghyuk.four_uni.user.domain.SignUserInfo;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.dto.response.EditResponse;
import bigsanghyuk.four_uni.user.dto.response.LoginResponse;
import bigsanghyuk.four_uni.user.dto.response.SignResponse;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
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
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    private final MailService mailService;

    private static final int EXPIRATION_IN_MINUTES = 43800;

    // 회원 가입
    public boolean register(SignUserInfo info) throws Exception {
        userRepository.findByEmail(info.getEmail())
                .ifPresent(user -> {
                    throw new EmailDuplicateException();
                });
        try {
            User user = User.builder()
                    .email(info.getEmail())
                    .password(encoder.encode(info.getPassword()))
                    .name(info.getName())
                    .departmentType(info.getDepartmentType())
                    .nickName(info.getNickName())
                    .image(info.getImage())
                    .build();
            user.setRoles(Collections.singletonList(
                    Authority.builder()
                            .name("ROLE_USER")
                            .build()));
            userRepository.save(user);
        } catch (Exception e) {
            log.error("e.getMessage={}", e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return true;
    }

    // 회원 정보 수정
    @Transactional
    public EditResponse edit(Long userId, EditUserInfo info) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.edit(encoder.encode(info.getPassword()), info.getName(), info.getDepartmentType(), info.getNickName(), info.getImage());
        User savedUser = userRepository.save(user);
        return EditResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .departmentType(savedUser.getDepartmentType())
                .nickName(savedUser.getNickName())
                .image(savedUser.getImage())
                .roles(savedUser.getRoles())
                .build();
    }

    // 로그인
    public LoginResponse login(LoginUserInfo info) {
        User user = userRepository.findByEmail(info.getEmail())
                .orElseThrow(UserNotFoundException::new);
        if (!encoder.matches(info.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정 정보입니다.");
        }
        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .departmentType(user.getDepartmentType())
                .nickName(user.getNickName())
                .image(user.getImage())
                .roles(user.getRoles())
                .token(TokenDto.builder()
                        .accessToken(jwtProvider.createToken(user.getEmail(), user.getRoles()))
                        .refreshToken(createRefreshToken(user))
                        .build())
                .build();
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
    public Token validRefreshToken(User user, String refreshToken) throws Exception {
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
        String email = jwtProvider.getEmail(tokenDto.getAccessToken());
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        Token token = validRefreshToken(user, tokenDto.getRefreshToken());
        if (token != null) {
            return TokenDto.builder()
                    .accessToken(jwtProvider.createToken(email, user.getRoles()))
                    .refreshToken(token.getRefreshToken())
                    .build();
        } else {
            throw new Exception("로그인이 필요합니다.");
        }
    }

    // 임시 비밀번호 발급 후 전송
    @Transactional
    public Boolean setToTempPassword(SendMailInfo sendMailInfo) {
        String email = sendMailInfo.getEmail();
        userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        String encodedTempPw = encoder.encode(mailService.sendTempPwMail(sendMailInfo));
        try {
            userRepository.updatePassword(email, encodedTempPw);
            return true;
        } catch (Exception e) {
            throw new IllegalStateException("오류가 발생했습니다.");
        }
    }

    // 비밀번호 변경
    @Transactional
    public Boolean changePassword(ChangePasswordInfo changePasswordInfo) throws IllegalAccessException {
        User user = userRepository.findById(changePasswordInfo.getId()).orElseThrow(UserNotFoundException::new);
        if (encoder.matches(changePasswordInfo.getOldPassword(), user.getPassword())) {
            userRepository.updatePassword(user.getEmail(), encoder.encode(changePasswordInfo.getNewPassword()));
            return true;
        } else {
            throw new IllegalAccessException("이전 비밀번호가 일치하지 않습니다.");
        }
    }
}
