package bigsanghyuk.four_uni.comment.controller;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.domain.LikeCommentInfo;
import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import bigsanghyuk.four_uni.comment.domain.UnLikeCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.comment.repository.LikeCommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class CommentControllerTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;
    @Autowired
    LikeCommentRepository likeCommentRepository;

    @Test
    @DisplayName("댓글 등록 성공")
    @WithMockUser
    void writeCommentSuccess() throws Exception {
        RegisterCommentInfo registerCommentInfo = new RegisterCommentInfo(1L, 1L, "content1", null, 0);
        mockMvc.perform(post("/posts/{postId}/comment", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerCommentInfo)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 등록 실패 - 없는 postId")
    @WithMockUser
    void writeCommentFail() throws Exception {
        RegisterCommentInfo registerCommentInfo = new RegisterCommentInfo(1L, 0L, "content1", null, 0);
        mockMvc.perform(post("/posts/{postId}/comment", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerCommentInfo)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    @WithMockUser
    void editCommentSuccess() throws Exception {
        EditCommentInfo editCommentInfo = new EditCommentInfo(1L, "editContent1");
        Comment originalComment = commentRepository.findById(1L).orElseThrow(CommentNotFoundException::new);
        mockMvc.perform(put("/posts/{postId}/{commentId}", "1", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editCommentInfo)))
                .andExpect(status().isOk());
        Comment foundComment = commentRepository.findById(1L).orElseThrow(CommentNotFoundException::new);
        Assertions.assertThat(foundComment.getContent()).isNotEqualTo(originalComment.getContent());
    }

    @Test
    @DisplayName("댓글 수정 실패 - 없는 postId")
    @WithMockUser
    void editCommentFail1() throws Exception {
        mockMvc.perform(put("/posts/{postId}/{commentId}", "0", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EditCommentInfo(1L, "content1"))))
                .andExpect(status().is4xxClientError());    //잘못된 postId
    }

    @Test
    @DisplayName("댓글 수정 실패 - 없는 commentId")
    @WithMockUser
    void editCommentFail2() throws Exception {
        mockMvc.perform(put("/posts/{postId}/{commentId}", "1", "0")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EditCommentInfo(1L, "content1"))))
                .andExpect(status().is4xxClientError());    //잘못된 commentId
    }

    @Test
    @DisplayName("댓글 수정 실패 - 없는 userId")
    @WithMockUser
    void editCommentFail3() throws Exception {
        mockMvc.perform(put("/posts/{postId}/{commentId}", "1", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EditCommentInfo(0L, "content1"))))
                .andExpect(status().is4xxClientError());    //잘못된 userId
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    @WithMockUser
    void deleteCommentSuccess() throws Exception {
        mockMvc.perform(delete("/posts/{postId}/{commentId}", "1", "1"))
                .andExpect(status().isOk());
        Assertions.assertThat(commentRepository.findById(1L)).isEmpty();
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 없는 postId")
    @WithMockUser
    void deleteCommentFail1() throws Exception {
        mockMvc.perform(delete("/posts/{postId}/{commentId}", "0", "1"))    //없는 postId
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 없는 commentId")
    @WithMockUser
    void deleteCommentFail2() throws Exception {
        mockMvc.perform(delete("/posts/{postId}/{commentId}", "1", "0"))    //없는 commentId
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("댓글 좋아요 성공")
    @WithMockUser
    void likeCommentSuccess() throws Exception {
        Long commentId = 1L;
        Comment originalComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        LikeCommentInfo likeCommentInfo = new LikeCommentInfo(1L, commentId);
        mockMvc.perform(post("/comments/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeCommentInfo)))
                .andExpect(status().isOk());
        Comment findComment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        Assertions.assertThat(findComment.getCommentLike()).isEqualTo(originalComment.getCommentLike() + 1);
    }

    @Test
    @DisplayName("댓글 좋아요 실패 - 없는 commentId")
    @WithMockUser
    void likeCommentFail1() throws Exception {
        Long commentId = 0L;
        LikeCommentInfo likeCommentInfo = new LikeCommentInfo(1L, commentId);
        mockMvc.perform(post("/comments/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeCommentInfo)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("댓글 좋아요 실패 - 이미 좋아요 한 댓글")
    @WithMockUser
    void likeCommentFail2() throws Exception {
        Long commentId = 1L;
        LikeCommentInfo likeCommentInfo = new LikeCommentInfo(1L, commentId);   // commentId = 1L인 댓글 좋아요
        mockMvc.perform(post("/comments/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeCommentInfo)))
                .andExpect(status().isOk());

        mockMvc.perform(post("/comments/like")  // commentId = 1L인 댓글 좋아요 한번 더 함
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeCommentInfo)))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("댓글 좋아요 취소 성공")
    @WithMockUser
    void unlikeCommentSuccess() throws Exception {
        Long commentId = 1L;
        LikeCommentInfo likeCommentInfo = new LikeCommentInfo(1L, commentId);
        mockMvc.perform(post("/comments/like")  // 좋아요 한 댓글 commentId = 1L
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeCommentInfo)))
                .andExpect(status().isOk());

        UnLikeCommentInfo unLikeCommentInfo = new UnLikeCommentInfo(1L, commentId);
        mockMvc.perform(delete("/comments/unlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(unLikeCommentInfo)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 좋아요 취소 실패 - 없는 commentId")
    @WithMockUser
    void unlikeCommentFail1() throws Exception {
        Long commentId = 1L;
        LikeCommentInfo likeCommentInfo = new LikeCommentInfo(1L, commentId);
        mockMvc.perform(post("/comments/like")  // 좋아요 한 댓글 commentId = 1L
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeCommentInfo)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/comments/unlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UnLikeCommentInfo(1L, 0L))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("댓글 좋아요 취소 실패 - 좋아요 하지 않은 댓글 좋아요 취소")
    @WithMockUser
    void unlikeCommentFail2() throws Exception {
        Long commentId = 1L;
        LikeCommentInfo likeCommentInfo = new LikeCommentInfo(1L, commentId);
        mockMvc.perform(post("/comments/like")  // 좋아요 한 댓글 commentId = 1L
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeCommentInfo)))
                .andExpect(status().isOk());

        mockMvc.perform(delete("/comments/unlike")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UnLikeCommentInfo(1L, 2L))))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("댓글 전체 조회 성공")
    @WithMockUser
    void getAllCommentsSuccess() throws Exception {
        String responseJson = mockMvc.perform(get("/posts/{postId}/comment", "1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(responseJson);
        Assertions.assertThat(responseJson).isNotEqualTo("{\"data\":[],\"count\":0}");
    }

    @Test
    @DisplayName("댓글 전체 조회 실패 - 없는 postId")
    @WithMockUser
    void getAllCommentsFail() throws Exception {
        mockMvc.perform(get("/posts/{postId}/comment", "0"))
                .andExpect(status().is4xxClientError());
    }

    @BeforeEach
    void beforeEach() {
        LocalDate dateNow = LocalDate.now();
        LocalDateTime datetimeNow = LocalDateTime.now();
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        postRepository.save(new Post(1L, false, "title1", "content1", "imageUrl1", 0, 0, true, dateNow, datetimeNow));
        postRepository.save(new Post(2L, false, "title2", "content2", "imageUrl2", 0, 0, true, dateNow, datetimeNow));
        commentRepository.save(new Comment(1L, 1L, null, 0, "content1", false));
        commentRepository.save(new Comment(1L, 2L, null, 0, "content2", false));
        commentRepository.save(new Comment(1L, 3L, null, 0, "content3", false));
        commentRepository.save(new Comment(1L, 4L, null, 0, "content4", false));
        likeCommentRepository.deleteAll();
    }
}