package bigsanghyuk.four_uni.service;

import bigsanghyuk.four_uni.config.jwt.domain.Token;
import bigsanghyuk.four_uni.config.jwt.dto.TokenDto;
import bigsanghyuk.four_uni.exception.jwt.TokenNotFoundException;
import bigsanghyuk.four_uni.exception.user.EmailDuplicateException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.user.domain.EditUserInfo;
import bigsanghyuk.four_uni.user.domain.LoginUserInfo;
import bigsanghyuk.four_uni.user.domain.SignUserInfo;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.dto.response.LoginResponse;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import bigsanghyuk.four_uni.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Slf4j
class UserServiceTest {

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    UserService service;
    @Autowired
    UserRepository repository;

    @Test
    @DisplayName("회원가입 성공")
    void registerSuccess() throws Exception {
        log.info("[existUser] email={}, password={}", "test@test.com", "test");
        log.info("[addUser] email={}, password={}", "test1@test.com", "test");
        SignUserInfo addUser = new SignUserInfo("test1@test.com", "test", "test", 10, "test", "test");
        service.register(addUser);
        List<User> allUsers = repository.findAll();
        for (User user : allUsers) {
            log.info("[user] email={}", user.getEmail());
        }
        assertThat(allUsers.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void registerDuplicate() {
        log.info("[existUser] email={}, password={}", "test@test.com", "test");
        log.info("[addUser] email={}, password={}", "test@test.com", "test");
        SignUserInfo addUser = new SignUserInfo("test@test.com", "test", "test", 10, "test", "test");
        assertThatThrownBy(() -> service.register(addUser))
                .isInstanceOf(EmailDuplicateException.class);
    }

    @Test
    @DisplayName("비밀번호 변경 성공")
    void updatePassword() {
        User originalUser = repository.findByEmail("test@test.com").get();
        String newPassword = "test1";
        service.edit(originalUser.getId(), new EditUserInfo(originalUser.getId(), newPassword, originalUser.getName(), originalUser.getDept(), originalUser.getNickName(), originalUser.getImage()));
        User updatedUser = repository.findByEmail("test@test.com").get();
        log.info("original Password={}", originalUser.getPassword());
        log.info("updated Password={}", updatedUser.getPassword());
        assertThat(originalUser.getPassword()).isNotEqualTo(updatedUser.getPassword());
        assertThat(encoder.matches(newPassword, originalUser.getPassword())).isFalse();
        assertThat(encoder.matches(newPassword, updatedUser.getPassword())).isTrue();
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        log.info("[existUser] email={}, password={}", "test@test.com", "test");
        log.info("[loginUser] email={}, password={}", "test@test.com", "test");
        LoginUserInfo loginUser = new LoginUserInfo("test@test.com", "test");
        Assertions.assertDoesNotThrow(() -> service.login(loginUser));
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 오류")
    void loginFailEmail() {
        log.info("[existUser] email={}, password={}", "test@test.com", "test");
        log.info("[loginUser] email={}, password={}", "test1@test.com", "test1");
        LoginUserInfo loginUser = new LoginUserInfo("test1@test.com", "test");
        assertThatThrownBy(() -> service.login(loginUser))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 오류")
    void loginFailPassword() {
        log.info("[existUser] email={}, password={}", "test@test.com", "test");
        log.info("[loginUser] email={}, password={}", "test@test.com", "test1");
        LoginUserInfo loginUser = new LoginUserInfo("test@test.com", "test1");
        assertThatThrownBy(() -> service.login(loginUser))
                .isInstanceOf(BadCredentialsException.class);
    }

    @Test
    @DisplayName("토큰 생성 성공")
    void createRefreshTokenSuccess() {
        User user = repository.findByEmail("test@test.com").get();
        String refreshToken = service.createRefreshToken(user);
        log.info("[RefreshToken] refreshToken={}", refreshToken);
        assertThat(refreshToken).isNotNull();
    }

    @Test
    @DisplayName("토큰 인증 성공")
    void validRefreshTokenSuccess() throws Exception {
        User user = repository.findByEmail("test@test.com").get();
        String refreshToken = service.createRefreshToken(user);
        log.info("[RefreshToken] refreshToken={}", refreshToken);

        Token token = service.validRefreshToken(user, refreshToken);
        assertThat(token.getRefreshToken()).isEqualTo(refreshToken);
    }

    @Test
    @DisplayName("토큰 인증 실패")
    void validRefreshTokenFail() {
        User user = User.builder()
                        .id(1234L).build();
        User user2 = User.builder()
                .id(12345L).build();
        String refreshToken = service.createRefreshToken(user);
        log.info("[RefreshToken] refreshToken={}", refreshToken);

        assertThatThrownBy(() -> service.validRefreshToken(user2, refreshToken)).isInstanceOf(TokenNotFoundException.class);
    }

    @Transactional
    @Test
    @DisplayName("액세스 토큰 갱신 성공")
    void refreshAccessTokenSuccess() throws Exception {
        LoginUserInfo loginUserInfo = new LoginUserInfo("test@test.com", "test");
        LoginResponse loginResponse = service.login(loginUserInfo);

        TokenDto tokenDto = loginResponse.getToken();
        log.info("[AccessToken] accessToken={}", tokenDto.getAccessToken());

        Thread.sleep(3000);

        TokenDto refreshedTokenDto = service.refreshAccessToken(tokenDto);
        log.info("[AccessToken] accessToken={}", refreshedTokenDto.getAccessToken());

        assertThat(tokenDto.getAccessToken()).isNotEqualTo(refreshedTokenDto.getAccessToken());
    }


    @BeforeEach
    void beforeEach() throws Exception {
        log.info("--- [beforeEach] add testUser ---");
        repository.deleteAll();
        service.register(new SignUserInfo("test@test.com", "test", "test", 10, "test", "test"));
    }

    @AfterEach
    void afterEach() {
        log.info("--- [afterEach] remove testUser ---");
        repository.deleteAll();
    }
}