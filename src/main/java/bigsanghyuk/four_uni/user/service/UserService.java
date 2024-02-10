package bigsanghyuk.four_uni.user.service;

import bigsanghyuk.four_uni.config.jwt.JwtProvider;
import bigsanghyuk.four_uni.config.jwt.domain.Token;
import bigsanghyuk.four_uni.config.jwt.domain.TokenRepository;
import bigsanghyuk.four_uni.config.jwt.dto.TokenDto;
import bigsanghyuk.four_uni.exception.jwt.TokenNotFoundException;
import bigsanghyuk.four_uni.exception.user.EmailDuplicateException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.user.domain.LoginUserInfo;
import bigsanghyuk.four_uni.user.domain.UpdateUserInfo;
import bigsanghyuk.four_uni.user.domain.RegisterUserInfo;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.dto.request.EditRequest;
import bigsanghyuk.four_uni.user.dto.request.LoginRequest;
import bigsanghyuk.four_uni.user.dto.request.SignRequest;
import bigsanghyuk.four_uni.user.dto.response.EditResponse;
import bigsanghyuk.four_uni.user.dto.response.LoginResponse;
import bigsanghyuk.four_uni.user.dto.response.SignResponse;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    private static final int EXPIRATION_IN_MINUTES = 43800;

    public void register(RegisterUserInfo registerUserInfo) {
        userRepository.findByEmail(registerUserInfo.getEmail())
                .ifPresent(user -> {
                    throw new EmailDuplicateException();
                });

        String encodedPassword = encoder.encode(registerUserInfo.getPassword());
        userRepository.save(
                new User(
                        registerUserInfo.getEmail(),
                        encodedPassword,
                        registerUserInfo.getName(),
                        registerUserInfo.getDept(),
                        registerUserInfo.getNickName(),
                        registerUserInfo.getImage()
                )
        );
    }

    public boolean register(SignRequest request) throws Exception {
        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new EmailDuplicateException();
                });
        try {
            LocalDateTime now = LocalDateTime.now();
            User user = User.builder()
                    .email(request.getEmail())
                    .password(encoder.encode(request.getPassword()))
                    .name(request.getName())
                    .dept(request.getDept())
                    .nickName(request.getNickName())
                    .image(request.getImage())
                    .createdAt(now)
                    .updatedAt(now)
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

    public void updateUser(UpdateUserInfo updateUserInfo) {

        User user = userRepository.findById(updateUserInfo.getId())
                .orElseThrow(UserNotFoundException::new);
        UpdateUserInfo encodedUpdateUser = new UpdateUserInfo(updateUserInfo.getId(), encoder.encode(updateUserInfo.getPassword()), updateUserInfo.getNickName(), updateUserInfo.getImage());
        user.edit(encodedUpdateUser);
        userRepository.save(user);
    }

    public EditResponse edit(EditRequest request) {
        User user = userRepository.findById(request.getId())
                .orElseThrow(UserNotFoundException::new);
        User source = userRepository.findById(request.getId()).get();
        user.edit(encoder.encode(request.getPassword()), request.getName(), request.getDept(), request.getNickName(), request.getImage());
        userRepository.save(user);
        return EditResponse.builder()
                .email(request.getEmail() == null ? source.getEmail() : request.getEmail())
                .name(request.getName() == null ? source.getName() : request.getName())
                .dept(request.getDept() == 0 ? source.getDept() : request.getDept())
                .nickName(request.getNickName() == null ? source.getNickName() : request.getNickName())
                .image(request.getImage() == null ? source.getImage() : request.getImage())
                .roles(source.getRoles())
                .build();
    }

    public boolean login(LoginUserInfo loginUserInfo) {
        Optional<User> user = userRepository.findByEmail(loginUserInfo.getEmail());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 이메일입니다.");
        }
        log.info("original password={}", loginUserInfo.getPassword());
        log.info("encoded password={}", user.get().getPassword());
        return encoder.matches(loginUserInfo.getPassword(), user.get().getPassword());
    }

    public LoginResponse login(LoginRequest request) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (optionalUser.isEmpty()) {
            throw new UsernameNotFoundException("존재하지 않는 이메일입니다.");
        }
        User user = optionalUser.get();
        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("잘못된 계정 정보입니다.");
        }
        user.setRefreshToken(createRefreshToken(user));
        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .dept(user.getDept())
                .nickName(user.getNickName())
                .image(user.getImage())
                .roles(user.getRoles())
                .token(TokenDto.builder()
                        .accessToken(jwtProvider.createToken(user.getEmail(), user.getRoles()))
                        .refreshToken(user.getRefreshToken())
                        .build())
                .build();
    }

    public SignResponse getMember(String email) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return new SignResponse(user);
    }

    public String createRefreshToken(User user) {
        Token token = tokenRepository.save(Token.builder()
                .id(user.getId())
                .refreshToken(UUID.randomUUID().toString())
                .expiration(EXPIRATION_IN_MINUTES)
                .build()
        );
        return token.getRefreshToken();
    }

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
}
