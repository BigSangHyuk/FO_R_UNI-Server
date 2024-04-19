package bigsanghyuk.four_uni.controller;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.config.TestSecurityConfig;
import bigsanghyuk.four_uni.config.jwt.JwtProvider;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
import bigsanghyuk.four_uni.post.service.ScrappedService;
import bigsanghyuk.four_uni.support.fixture.PostEntityFixture;
import bigsanghyuk.four_uni.support.fixture.UserEntityFixture;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@Import(TestSecurityConfig.class)
@AutoConfigureMockMvc
@Transactional
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ScrappedService scrappedService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ScrappedRepository scrappedRepository;
    
    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this); // 목 객체 초기화
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @Test
    void 게시글_등록() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_ADMIN.UserEntity_생성(1L));
        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_ADMIN");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), Collections.singletonList(Authority.builder().name("ROLE_ADMIN").build()));

        String testData =
                "[\n" +
                "    {\n" +
                "        \"category_id\": \"246\",\n" +
                "        \"title\": \"대학수학  튜터시간표\",\n" +
                "        \"content\": \"\\nTutor시간표 대학수학(1)\\n요일\\n월\\n화\\n수\\n목\\n금\\n시간\\n10:00~12:00/\\n14:00~16:00\\n튜터실\\n5-106호\\n※튜터실은5호관(자연과학대학)입니다.\\n\",\n" +
                "        \"img_url\": [],\n" +
                "        \"posted_at\": \"2024.03.28\",\n" +
                "        \"deadline\": \"2024.03.28\",\n" +
                "        \"isclassified\": true,\n" +
                "        \"notice_url\": \"https://inu.ac.kr/bbs/inu/246/385074/artclView.do\"\n" +
                "    }\n" +
                "]";

        //when, then
        mockMvc.perform(post("/add-post")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .content(testData)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authentication(atc)))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertThat(postRepository.findAll().contains("246"));

    }

    @Test
    void 게시글_댓글_함께_조회() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_NORMAL.UserEntity_생성(2L));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        Post post = postRepository.save(PostEntityFixture.POST_ISIS.PostEntity_생성());
        commentRepository.save(new Comment(1L, user, post.getId(), false, null, 0, "testContent", 0, false));

        //when, then
        mockMvc.perform(get("/posts/{postId}", post.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authentication(atc)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 스크랩_추가_성공() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_NORMAL.UserEntity_생성(3L));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        Post post = postRepository.save(PostEntityFixture.POST_ISIS.PostEntity_생성());

        //when, then
        mockMvc.perform(post("/posts/scrap/{postId}", post.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(authentication(atc)))
                        .andExpect(status().isOk())
                        .andDo(print());

        Assertions.assertThat(scrappedRepository.findAll().contains(post.getId()));
    }

    @Test
    void 이미_스크랩한_글_예외() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_NORMAL.UserEntity_생성(4L));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        Post post = postRepository.save(PostEntityFixture.POST_ACADEMY.PostEntity_생성());
        scrappedService.scrap(user.getId(), post.getId());

        //when
        ResultActions resultActions = mockMvc.perform(post("/posts/scrap/{postId}", post.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(authentication(atc)))
                .andExpect(status().is4xxClientError())
                .andDo(print());

        MvcResult mvcResult = resultActions.andReturn();
        String responseBody = mvcResult.getResponse().getContentAsString(UTF_8);

        // then
        resultActions.andExpect(status().is4xxClientError())
                .andDo(print());
        assertTrue(responseBody.contains("이미 스크랩한 글입니다."));
    }
    @Test
    void 스크랩_취소_테스트() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_NORMAL.UserEntity_생성(5L));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        Post post = postRepository.save(PostEntityFixture.POST_ACADEMY.PostEntity_생성());
        scrappedService.scrap(user.getId(), post.getId());

        //when, then
        mockMvc.perform(delete("/posts/unscrap/{postId}", post.getId())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authentication(atc)))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertThat(scrappedRepository.findAll().isEmpty());
    }

    @Test
    void 게시글_필터_조회() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_NORMAL.UserEntity_생성(6L));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        postRepository.save(PostEntityFixture.POST_ACADEMY.PostEntity_생성());
        postRepository.save(PostEntityFixture.POST_ACADEMY_2.PostEntity_생성());

        //when, then
        mockMvc.perform(get("/posts/filtered?id=246")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(authentication(atc)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 댓글_남긴_글_조회() throws Exception {
        //given
        User user = userRepository.save(UserEntityFixture.USER_NORMAL.UserEntity_생성(7L));

        Authentication atc = new TestingAuthenticationToken("test_email@test.com", null, "ROLE_USER");
        String accessToken = jwtProvider.createToken(user.getEmail(), user.getId(), user.getRoles());

        Post postIs = postRepository.save(PostEntityFixture.POST_ISIS.PostEntity_생성());
        commentRepository.save(new Comment(2L, user, postIs.getId(), false, null, 0, "testContent", 0, false));

        Post postAcademy = postRepository.save(PostEntityFixture.POST_ACADEMY.PostEntity_생성());
        commentRepository.save(new Comment(3L, user, postAcademy.getId(), false, null, 3, "testContent2", 0, false));

        //when, then
        mockMvc.perform(get("/posts/commented")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(authentication(atc)))
                .andExpect(status().isOk())
                .andDo(print());
    }

/*
사용할 Json 형태의 게시글
[
    {
        "category_id": "246",
        "title": "대학수학  튜터시간표",
        "content": "\nTutor시간표 대학수학(1)\n요일\n월\n화\n수\n목\n금\n시간\n10:00~12:00/\n14:00~16:00\n튜터실\n5-106호\n※튜터실은5호관(자연과학대학)입니다.\n",
        "img_url": [],
        "posted_at": "2024.03.28",
        "deadline": "2024.03.28",
        "isclassified": true,
        "notice_url": "https://inu.ac.kr/bbs/inu/246/385074/artclView.do"
    }
]
[
    {
        "category_id": "246",
        "title": "2024학년도 1학기 수강포기 처리완료 안내",
        "content": "\n2024학년도 1학기 수강포기 처리완료 안내\n2024학년도 1학기 수강포기 신청에 대한 처리결과를 아래와 같이 안내합니다.\n- 수강포기 결과 확인: 통합정보 → 학사행정 → 학적 → 학적기본관리 → 개인학적조회(학생)\n",
        "img_url": [],
        "posted_at": "2024.03.28",
        "deadline": "",
        "isclassified": false,
        "notice_url": "https://inu.ac.kr/bbs/inu/246/385042/artclView.do"
    }
]
 */
}
