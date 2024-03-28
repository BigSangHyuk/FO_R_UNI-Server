package bigsanghyuk.four_uni.controller;

import bigsanghyuk.four_uni.user.domain.LoginUserInfo;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.enums.CategoryType;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import bigsanghyuk.four_uni.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        userRepository.save(User.builder()
                .id(1L)
                .name("test_name")
                .email("test_email@test.com")
                .password(passwordEncoder.encode("test1111"))
                .departmentType(CategoryType.ISIS) // 컴퓨터 공학부
                .image("test_image_url")
                .nickName("test_nickname")
                .build());
    }

    @AfterEach
    void afterEach() {
        // 테스트 종료 후 데이터 정리
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    void signUpSuccess() throws Exception {
        //given
        User user = User.builder()
                .id(2L)
                .name("test_name2")
                .email("test_email2@test.com")
                .password("test2222")
                .departmentType(CategoryType.ISIS) // 컴퓨터 공학부
                .image("test_image_url2")
                .nickName("test_nickname2")
                .build();

        user.setRoles(Collections.singletonList(
                Authority.builder()
                        .name("ROLE_USER")
                        .build()));

        // Convert User object to JSON
        String content = objectMapper.writeValueAsString(user);

        //when, then
        mockMvc.perform(post("/sign-up")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }


    @Test
    @DisplayName("로그인")
    public void signIn() throws Exception {
        //given
        LoginUserInfo info = new LoginUserInfo("test_email@test.com", "test1111");

        //when, then
        mockMvc.perform(post("/sign-in")
                        .content(objectMapper.writeValueAsString(info))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(status().isOk());
    }
}