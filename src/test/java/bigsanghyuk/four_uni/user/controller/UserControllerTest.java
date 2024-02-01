package bigsanghyuk.four_uni.user.controller;

import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import bigsanghyuk.four_uni.user.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class UserControllerTest {

    User user;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    public void registerSetUp(){ //테스트 실행할땨마다 수행시킴

        String randomCode = RandomStringUtils.randomAlphanumeric(15);

        Random random = new Random();
        Long randomNum = random.nextLong();
        user = User
                .builder()
                .id(randomNum)
                .name("test_name")
                .email("test_email")
                .password(randomCode)
                .dept(1)
                .nickName("test_nickname")
                .createdAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void register() {
        User savedUser = userRepository.save(user);

        assertNotNull(savedUser.getId());

        User retrievedUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(retrievedUser);
    }

}
