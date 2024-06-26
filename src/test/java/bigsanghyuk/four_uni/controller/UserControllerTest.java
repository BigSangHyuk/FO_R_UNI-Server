package bigsanghyuk.four_uni.controller;

import bigsanghyuk.four_uni.config.jwt.JwtProvider;
import bigsanghyuk.four_uni.support.fixture.UserEntityFixture;
import bigsanghyuk.four_uni.user.domain.ChangePasswordInfo;
import bigsanghyuk.four_uni.user.domain.EditUserInfo;
import bigsanghyuk.four_uni.user.domain.LoginUserInfo;
import bigsanghyuk.four_uni.user.domain.SignUserInfo;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.enums.CategoryType;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import bigsanghyuk.four_uni.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
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

import java.util.Collections;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void init() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @AfterEach
    void afterEach() {
        // 테스트 종료 후 데이터 정리
        userRepository.deleteAll();
    }

    @Test
    void 회원가입_성공() throws Exception {
        //given
        SignUserInfo info = new SignUserInfo("test_email2@test.com",  passwordEncoder.encode("test2222"), CategoryType.ISIS, null, "test_nickname2", "test_image_url2");

        //when, then
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
        userRepository.save(new User(1L, "test_email@test.com", passwordEncoder.encode("test2222"), CategoryType.ISIS, null, "test_nickname3", "test_image_url3", Collections.singletonList(Authority.builder().name("ROLE_USER").build())));
        SignUserInfo info = new SignUserInfo("test_email@test.com", passwordEncoder.encode("test2222"), CategoryType.ISIS, null, "test_nickname4", "test_image_url4");

        //when
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
    void 로그인_성공() throws Exception {
        //given
        User user = userRepository.save(new User(1L, "test_email@test.com", passwordEncoder.encode("test2222"), CategoryType.ISIS, null, "test_nickname3", "test_image_url3", Collections.singletonList(Authority.builder().name("ROLE_USER").build())));

        LoginUserInfo info = new LoginUserInfo(user.getEmail(), "test2222");

        //when
        ResultActions resultActions = mockMvc.perform(post("/sign-in")
                .content(objectMapper.writeValueAsString(info))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString(UTF_8);

        // then
        resultActions.andExpect(status().isOk())
                .andDo(print());

        assertTrue(responseBody.contains("accessToken"));
        assertTrue(responseBody.contains("refreshToken"));
    }

    @Test
    void 비밀번호_불일치로_로그인_실패() throws Exception {
        //given
        userRepository.save(new User(1L, "test_email@test.com", passwordEncoder.encode("test2222"), CategoryType.ISIS, null, "test_nickname3", "test_image_url3", Collections.singletonList(Authority.builder().name("ROLE_USER").build())));
        LoginUserInfo info = new LoginUserInfo("test_email@test.com", passwordEncoder.encode("test"));

        //when
        ResultActions resultActions = mockMvc.perform(post("/sign-in")
                        .content(objectMapper.writeValueAsString(info))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString(UTF_8);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
        assertTrue(responseBody.contains("비밀번호가 일치하지 않습니다."));
    }

    @Test
    void 존재하지_않는_이메일로_인한_로그인_실패() throws Exception {
        //given
        userRepository.save(new User(1L, "test_email@test.com", passwordEncoder.encode("test2222"), CategoryType.ISIS, null, "test_nickname3", "test_image_url3", Collections.singletonList(Authority.builder().name("ROLE_USER").build())));
        LoginUserInfo info = new LoginUserInfo("test_email2@test.com", passwordEncoder.encode("testPassword"));

        //when
        ResultActions resultActions = mockMvc.perform(post("/sign-in")
                .content(objectMapper.writeValueAsString(info))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString(UTF_8);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
        assertTrue(responseBody.contains("존재하지 않는 사용자입니다."));
    }

    @Test
    void 탈퇴_성공() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_NORMAL.UserEntity_생성(1L));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        //when, then
        mockMvc.perform(delete("/leave")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .content(String.valueOf(user.getId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authentication(atc)))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertThat(userRepository.findByEmail("test_email@test.com").isEmpty());
    }

    @Test
    void 회원_정보_수정() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_NORMAL.UserEntity_생성(1L));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        EditUserInfo info = new EditUserInfo(CategoryType.ARCHI, null, "testChangeNickName");

        //when, then
        mockMvc.perform(patch("/users/edit")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(info))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(authentication(atc)))
                .andExpect(status().isOk())
                .andDo(print());

        assertThat(userRepository.findByNickName("testChangeNickName")).isNotEmpty();
    }

    @Test
    void 비밀번호_변경_성공() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_NORMAL.UserEntity_생성(1L));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        ChangePasswordInfo info = new ChangePasswordInfo("testPassword", "newTestPassword");

        //when, then
        ResultActions actions = mockMvc.perform(patch("/users/password")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(info))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(authentication(atc)));

        MvcResult result = actions
                .andExpect(status().is4xxClientError())
                .andDo(print())
                .andReturn();

        String content = result.getResponse().getContentAsString();

        log.info(content);
    }

    @Test
    void 이전_비밀번호_불일치로_인해_실패() throws Exception {
        //given
        User user = userRepository.save(new User(1L, "test_email@test.com", passwordEncoder.encode("test2222"), CategoryType.ISIS, null, "test_nickname3", "test_image_url3", Collections.singletonList(Authority.builder().name("ROLE_USER").build())));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        ChangePasswordInfo info = new ChangePasswordInfo(user.getPassword(), passwordEncoder.encode("newPassword"));

        //when
        ResultActions resultActions = mockMvc.perform(put("/users/password")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .content(objectMapper.writeValueAsString(info))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authentication(atc)));

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString(UTF_8);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
        assertTrue(responseBody.contains("이전 비밀번호와 일치하지 않습니다."));
    }

    @Test
    void 일반_유저_본인_정보_조회() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_NORMAL.UserEntity_생성(6L));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        //when, then
        mockMvc.perform(get("/users/info")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authentication(atc)))
                .andExpect(status().isOk())
                .andDo(print());
    }
}