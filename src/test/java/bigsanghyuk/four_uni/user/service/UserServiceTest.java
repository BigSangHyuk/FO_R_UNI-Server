package bigsanghyuk.four_uni.user.service;

import bigsanghyuk.four_uni.user.domain.LoginUserInfo;
import bigsanghyuk.four_uni.user.domain.RegisterUserInfo;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.dto.request.LoginUserRequest;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
    void registerSuccess() {
        log.info("[existUser] email={}, password={}", "test@test.com", "test");
        log.info("[addUser] email={}, password={}", "test1@test.com", "test");
        RegisterUserInfo addUser = new RegisterUserInfo("test1@test.com", "test", "test", 10, "test", "test");
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
        RegisterUserInfo addUser = new RegisterUserInfo("test@test.com", "test", "test", 10, "test", "test");
        assertThatThrownBy(() -> service.register(addUser))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() {
        log.info("[existUser] email={}, password={}", "test@test.com", "test");
        log.info("[loginUser] email={}, password={}", "test@test.com", "test");
        LoginUserInfo loginUser = new LoginUserInfo("test@test.com", "test");
        assertThat(service.login(loginUser)).isTrue();
    }

    @Test
    @DisplayName("로그인 실패 - 이메일 오류")
    void loginFailEmail() {
        log.info("[existUser] email={}, password={}", "test@test.com", "test");
        log.info("[loginUser] email={}, password={}", "test1@test.com", "test1");
        LoginUserInfo loginUser = new LoginUserInfo("test1@test.com", "test");
        assertThatThrownBy(() -> service.login(loginUser))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    @Test
    @DisplayName("로그인 실패 - 비밀번호 오류")
    void loginFailPassword() {
        log.info("[existUser] email={}, password={}", "test@test.com", "test");
        log.info("[loginUser] email={}, password={}", "test@test.com", "test1");
        LoginUserInfo loginUser = new LoginUserInfo("test@test.com", "test1");
        assertThat(service.login(loginUser)).isFalse();
    }

    @BeforeEach
    void beforeEach() {
        log.info("--- [beforeEach] add testUser ---");
        service.register(new RegisterUserInfo("test@test.com", "test", "test", 10, "test", "test"));
    }

    @AfterEach
    void afterEach() {
        log.info("--- [afterEach] remove testUser ---");
        repository.deleteAll();
    }
}