package bigsanghyuk.four_uni.controller;


import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.post.domain.ScrapInfo;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.net.URLDecoder.decode;
import static java.net.URLEncoder.encode;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class PostControllerTest {

    static String encodedWithBracket = encode("{1,2,3,4}", StandardCharsets.UTF_8);
    static String encodedWithoutBracket = encode("1,2,3,4", StandardCharsets.UTF_8);
    static String queryParameter = "1-2-3-4";

    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;

    @Test
    @DisplayName("괄호 있는 인코딩")
    void urlEncodingWithBracket() {
        long startTime = System.nanoTime();

        String decoded = decode(encodedWithBracket, StandardCharsets.UTF_8);
        String substring = decoded.substring(1, decoded.length() - 1);
        List<Long> result = parseLongList(substring);

        long endTime = System.nanoTime();
        System.out.printf("괄호 있는 인코딩: %dns%n", endTime - startTime);
    }

    @Test
    @DisplayName("괄호 없는 인코딩")
    void test() {
        long startTime = System.nanoTime();

        String decoded = decode(encodedWithoutBracket, StandardCharsets.UTF_8);
        List<Long> result = parseLongList(decoded);

        long endTime = System.nanoTime();
        System.out.printf("괄호 없는 인코딩: %dns%n", endTime - startTime);
    }

    @Test
    @DisplayName("하이픈 문자열")
    void queryParam() {
        long startTime = System.nanoTime();

        List<Long> result = parseLongList(queryParameter, "-");

        long endTime = System.nanoTime();
        System.out.printf("하이픈 문자열: %dns%n", endTime - startTime);
    }

    private static List<Long> parseLongList(String input) {
        List<Long> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            result.add(Long.parseLong(matcher.group()));
        }
        return result;
    }

    private static List<Long> parseLongList(String input, String delimiter) {
        List<Long> result = new ArrayList<>();
        String[] tokens = input.split(delimiter);
        for (String token : tokens) {
            result.add(Long.parseLong(token));
        }
        return result;
    }

    @Test
    @DisplayName("댓글 단 글 전부 조회 성공")
    @WithMockUser
    void getPostsCommentedSuccess() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/posts/commented")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andDo(print());
        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println(contentAsString);
        Assertions.assertThat(!contentAsString.equals("{\"data\":[],\"count\":0}"));
    }

    @Test
    @DisplayName("댓글 단 글 전부 조회 실패 - 없는 userId, 반환되는 데이터 수: 0")
    @WithMockUser
    void getPostsCommentedFail() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/posts/commented")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .param("userId", "-1"))
                .andExpect(status().isOk())
                .andDo(print());
        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(contentAsString.equals("{\"data\":[],\"count\":0}"));
    }

    @Test
    @DisplayName("게시글 조회 성공")
    @WithMockUser
    void getDetailSuccess() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/posts/{postId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andDo(print());
        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("response=" + contentAsString);
    }

    @Test
    @DisplayName("게시글 조회 실패 - 없는 postId")
    @WithMockUser
    void getDetailFail() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/posts")
                        .param("postId", "-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().is4xxClientError());
        MvcResult mvcResult = resultActions.andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("response=" + contentAsString);
    }

    @Test
    @DisplayName("미분류 게시글 조회 성공")
    @WithMockUser
    void getUnclassified() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/posts/unclassified"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("response=" + contentAsString);
        Assertions.assertThat(!contentAsString.equals("{\"data\":[],\"count\":0}"));
    }

    @Test
    @DisplayName("게시글 필터로 조회 성공")
    @WithMockUser
    void getByFilterSuccess() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/posts/filter")
                        .param("id", "1-2-3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("response=" + contentAsString);
        Assertions.assertThat(!contentAsString.equals("{\"data\":[],\"count\":0}"));
    }

    @Test
    @DisplayName("스크랩 추가 성공")
    @WithMockUser
    void scrapSuccess() throws Exception {
        ScrapInfo scrapInfo = new ScrapInfo(1L, 1L);
        mockMvc.perform(post("/posts/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scrapInfo)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("스크랩 추가 실패 - 없는 postId")
    @WithMockUser
    void scrapFail() throws Exception {
        ScrapInfo scrapInfo = new ScrapInfo(1L, 0L);
        mockMvc.perform(post("/posts/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scrapInfo)))
                .andExpect(status().is4xxClientError())
                .andReturn();
    }

    @Test
    @DisplayName("스크랩 해제 성공")
    @WithMockUser
    void unscrapSuccess() throws Exception {
        ScrapInfo scrapInfo = new ScrapInfo(1L, 1L);
        mockMvc.perform(post("/posts/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scrapInfo)))
                .andExpect(status().isOk())
                .andReturn();
        mockMvc.perform(delete("/posts/unscrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(scrapInfo)))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @DisplayName("스크랩한 게시글 조회 성공")
    @WithMockUser
    void getScrappedSuccess() throws Exception {
        mockMvc.perform(post("/posts/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ScrapInfo(1L, 1L))))
                .andExpect(status().isOk());
        mockMvc.perform(post("/posts/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ScrapInfo(1L, 2L))))
                .andExpect(status().isOk());

        MvcResult mvcResult = mockMvc.perform(get("/posts/scrapped")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Assertions.assertThat(!contentAsString.equals("{\"data\":[],\"count\":0}"));
        System.out.println(contentAsString);
    }

    @BeforeEach
    void beforeEach() {
        LocalDate dateNow = LocalDate.now();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        postRepository.save(new Post(1L, false, "title1", "content1", List.of("imageUrl1"), 0, 0, true, dateNow, dateNow));
        postRepository.save(new Post(2L, false, "title2", "content2", List.of("imageUrl2"), 0, 0, true, dateNow, dateNow));
        postRepository.save(new Post(2L, false, "title3", "content3", List.of("imageUrl3"), 0, 0, true, dateNow, dateNow));
        postRepository.save(new Post(3L, false, "title4", "content4", List.of("imageUrl4"), 0, 0, true, dateNow, dateNow));
        postRepository.save(new Post(3L, false, "title5", "content5", List.of("imageUrl5"), 0, 0, false, dateNow, dateNow));
        commentRepository.save(new Comment(1L, 1L, null, 0, "content1", false));
        commentRepository.save(new Comment(1L, 2L, null, 0, "content2", false));
        commentRepository.save(new Comment(1L, 3L, null, 0, "content3", false));
        commentRepository.save(new Comment(1L, 4L, null, 0, "content4", false));
    }
}