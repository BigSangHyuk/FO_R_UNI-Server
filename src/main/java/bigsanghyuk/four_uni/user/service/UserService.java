package bigsanghyuk.four_uni.user.service;

import bigsanghyuk.four_uni.config.jwt.JwtProvider;
import bigsanghyuk.four_uni.config.jwt.domain.Token;
import bigsanghyuk.four_uni.config.jwt.domain.TokenRepository;
import bigsanghyuk.four_uni.config.jwt.dto.TokenDto;
import bigsanghyuk.four_uni.exception.jwt.TokenNotFoundException;
import bigsanghyuk.four_uni.exception.user.EmailDuplicateException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
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

    private static final int EXPIRATION_IN_MINUTES = 43800;

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
                    .dept(info.getDept())
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

    @Transactional
    public EditResponse edit(Long userId, EditUserInfo info) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        user.edit(encoder.encode(info.getPassword()), info.getName(), info.getDept(), info.getNickName(), info.getImage());
        User savedUser = userRepository.save(user);
        return EditResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .dept(savedUser.getDept())
                .nickName(savedUser.getNickName())
                .image(savedUser.getImage())
                .roles(savedUser.getRoles())
                .build();
    }

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
                .dept(user.getDept())
                .nickName(user.getNickName())
                .image(user.getImage())
                .roles(user.getRoles())
                .token(TokenDto.builder()
                        .accessToken(jwtProvider.createToken(user.getEmail(), user.getRoles()))
                        .refreshToken(createRefreshToken(user))
                        .build())
                .build();
    }

    public SignResponse getUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
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
