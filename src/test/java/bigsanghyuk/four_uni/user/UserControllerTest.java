package bigsanghyuk.four_uni.user;

import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.enums.CategoryType;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import bigsanghyuk.four_uni.user.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Random;

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
                .departmentType(CategoryType.ISIS) // 컴퓨터 공학부
                .image("test_image_url")
                .nickName("test_nickname")
                .build();
    }

    @AfterEach
    void afterEach() {
        // 테스트 종료 후 데이터 정리
        userRepository.deleteAll();
    }

}
