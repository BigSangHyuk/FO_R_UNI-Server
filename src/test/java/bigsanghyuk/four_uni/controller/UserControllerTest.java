package bigsanghyuk.four_uni.controller;

import bigsanghyuk.four_uni.user.domain.LoginUserInfo;
import bigsanghyuk.four_uni.user.domain.SignUserInfo;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.enums.CategoryType;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import bigsanghyuk.four_uni.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
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
    void 회원가입_성공() throws Exception {
        //given
        SignUserInfo info = new SignUserInfo("test_email2@test.com", "test2222", CategoryType.ISIS, "test_nickname2", "test_image_url2");

        // when, then
        mockMvc.perform(post("/sign-up")
                    .content(objectMapper.writeValueAsString(info))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 이미_존재하는_이메일로_회원가입시_예외발생() throws Exception {
        //given
        SignUserInfo info = new SignUserInfo("test_email@test.com", "test2222", CategoryType.ISIS, "test_nickname3", "test_image_url3");

        // when
        ResultActions resultActions = mockMvc.perform(post("/sign-up")
                .content(objectMapper.writeValueAsString(info))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString(UTF_8);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
        assertTrue(responseBody.contains("이미 존재하는 이메일입니다."));
    }

    @Test
    public void 로그인_성공() throws Exception {
        //given
        LoginUserInfo info = new LoginUserInfo("test_email@test.com", "test1111");

        //when, then
        mockMvc.perform(post("/sign-in")
                        .content(objectMapper.writeValueAsString(info))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
}