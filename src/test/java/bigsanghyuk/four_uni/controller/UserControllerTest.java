package bigsanghyuk.four_uni.controller;

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
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserControllerTest {

    User user;
    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;

    @BeforeEach // 테스트 실행할때마다 수행
    public void registerSetUp() {
        // given : 회원 가입 테스트를 위한 초기 설정
        String randomCode = RandomStringUtils.randomAlphanumeric(15);

        Random random = new Random();
        Long randomNum = random.nextLong();
        user = User.builder()
                .id(randomNum)
                .name("test_name")
                .email("test_email@test.com")
                .password(randomCode)
                .dept(1)
                .image("test_image_url")
                .nickName("test_nickname")
                .build();
    }

    @AfterEach
    void afterEach() {
        // 테스트 종료 후 데이터 정리
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void register() {
        // when : 회원 가입 동작 수행
        User savedUser = userRepository.save(user);

        // then : 회원 가입 후에는 ID가 존재해야 하고 저장된 회원 정보를 조회하여 존재해야 함
        assertNotNull(savedUser.getId());

        User retrievedUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(retrievedUser);
    }

    @Test
    @DisplayName("회원정보수정")
    void update() {
        // given : 회원 정보 저장
        User savedUser = userRepository.save(user);

        // and : 수정할 정보
        String updatedName = "updated_name";
        String updatedEmail = "updated_email@test.com";
        String updatedNickName = "updated_nickname";

        // when : 회원 정보 업데이트 동작 수행
        savedUser.setName(updatedName);
        savedUser.setEmail(updatedEmail);
        savedUser.setNickName(updatedNickName);

        userRepository.save(savedUser);

        // then : 업데이트된 회원 정보 확인
        User updatedUser = userRepository.findById(savedUser.getId()).orElse(null);
        assertNotNull(updatedUser);
        assertEquals(updatedName, updatedUser.getName());
        assertEquals(updatedEmail, updatedUser.getEmail());
        assertEquals(updatedNickName, updatedUser.getNickName());
    }

    @Test
    @DisplayName("회원정보삭제")
    void delete() {
        // given : 회원 정보 저장
        User savedUser = userRepository.save(user);

        // when : 회원 정보 삭제 동작 수행
        userRepository.deleteById(savedUser.getId());

        // then : 삭제된 회원 정보 조회 시 null이 반환되어야 함
        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }

}
