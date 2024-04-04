package bigsanghyuk.four_uni.controller;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.config.TestSecurityConfig;
import bigsanghyuk.four_uni.post.controller.PostController;
import bigsanghyuk.four_uni.post.domain.ScrapInfo;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.Scrapped;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
import bigsanghyuk.four_uni.post.service.PostService;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.enums.CategoryType;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(TestSecurityConfig.class)
@Transactional
public class PostControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PostController postController;

    @Mock
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ScrappedRepository scrappedRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private WebApplicationContext wac;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this); // 목 객체 초기화
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        Post post = postRepository.save(Post.builder()
                        .id(1L)
                        .categoryType(CategoryType.ISIS)
                        .reported(false)
                        .title("testPostTitle")
                        .content("testContent")
                        .imageUrl(Collections.singletonList("testImageUrl"))
                        .views(0)
                        .postReportCount(0)
                        .isClassified(false)
                        .postedAt(LocalDate.now())
                        .deadline(LocalDate.now())
                        .noticeUrl("testNoticeUrl")
                .build());

        User user = userRepository.save(User.builder()
                .id(1L)
                .name("test_name")
                .email("test_email@test.com")
                .password(passwordEncoder.encode("test1111"))
                .departmentType(CategoryType.ISIS) // 컴퓨터 공학부
                .image("test_image_url")
                .nickName("test_nickname")
                .build());

        commentRepository.save(Comment.builder()
                .id(1L)
                .user(user)
                .postId(1L)
                .reported(false)
                .parent(null)
                .commentLike(0)
                .content("testComment")
                .commentReportCount(0)
                .deleted(false)
                .build());

        scrappedRepository.save(new Scrapped(user, post));
    }

    @AfterEach
    void afterEach() {
        // 테스트 종료 후 데이터 정리
        commentRepository.deleteAll();
        postRepository.deleteAll();
        userRepository.deleteAll();
        scrappedRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 등록")
    void postRegisterSuccess() throws Exception {
        //given
        Authentication authentication = new TestingAuthenticationToken("test1@gmail.com", null, "ROLE_ADMIN");

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
        ResultActions actions = mockMvc.perform(post("/add-post")
                        .content(testData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .with(authentication(authentication))
        );

        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글과 댓글 함께 조회하는 테스트")
    void getPostWithCommentsSuccess() throws Exception {
        //given
        Authentication authentication = new TestingAuthenticationToken("test1@gmail.com", null, "ROLE_ADMIN");

        //when, then
        ResultActions actions = mockMvc.perform(get("/posts/{postId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authentication(authentication))

        );

        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("스크랩 추가 테스트")
    void scrapSuccess() throws Exception {
        //given
        Authentication authentication = new TestingAuthenticationToken("test1@gmail.com", null, "ROLE_ADMIN");

        Post post = postRepository.save(new Post(2L, CategoryType.ISIS, false, "testPostTitle2", "testContent2", Collections.singletonList("testImageUrl2"), 0, 0, false, LocalDate.now(), LocalDate.now(), "testNoticeUrl2"));

        //when, then
        ResultActions actions = mockMvc.perform(post("/posts/scrap/{postId}", post.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authentication(authentication))
        );

        actions
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("스크랩 취소 테스트")
    void unScrapSuccess() throws Exception {
        //given
        Authentication authentication = new TestingAuthenticationToken("test1@gmail.com", null, "ROLE_ADMIN");

        //when, then
        ResultActions actions = mockMvc.perform(delete("/posts/unscrap/{postId}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .with(authentication(authentication))
        );

        actions
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
