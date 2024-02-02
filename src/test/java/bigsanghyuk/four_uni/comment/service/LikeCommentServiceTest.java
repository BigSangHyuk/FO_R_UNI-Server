package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.LikeCommentInfo;
import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.domain.entity.LikeComment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.comment.repository.LikeCommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class LikeCommentServiceTest {

    Comment comment;
    @Autowired
    LikeCommentService likeCommentService;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    LikeCommentRepository likeCommentRepository;

    @DisplayName("댓글 좋아요 성공")
    @Test
    void likeCommentSuccess() {
        log.info("[likeCommentSuccess]");
        Long commentId = comment.getId();
        log.info("likeCount={}", comment.getCommentLike());
        likeCommentService.likeComment(new LikeCommentInfo(1L, commentId));
        Comment findComment = commentRepository.findByUserId(comment.getUserId()).get();
        log.info("likeCount={}", findComment.getCommentLike());
        assertThat(findComment.getCommentLike()).isEqualTo(1);
    }

    @DisplayName("없는 댓글 Id")
    @Test
    void noExistComment() {
        log.info("[noExistComment]");
        assertThatThrownBy(() -> likeCommentService.likeComment(new LikeCommentInfo(1L, 2L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("이미 좋아요 누른 댓글")
    void alreadyLikedComment() {
        log.info("[alreadyLikedComment]");
        likeCommentService.likeComment(new LikeCommentInfo(1L, comment.getId()));
        assertThatThrownBy(() -> likeCommentService.likeComment(new LikeCommentInfo(1L, 1L)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @BeforeEach
    void beforeEach() {
        log.info("[beforeEach]");
        // given : 댓글 컨트롤러 테스트를 위한 초기 설정
        String randomCode = RandomStringUtils.randomAlphanumeric(15);
        comment = Comment.builder()
                .id(1L)
                .userId(1L)
                .postId(2L)
                .parentCommentId(3L)
                .commentLike(0)
                .content(randomCode)
                .commentReportCount(0)
                .createdAt(LocalDateTime.now())
                .build();
        commentRepository.save(comment);
    }

    @AfterEach
    void afterEach() {
        log.info("[afterEach]");
        commentRepository.deleteAll();
        likeCommentRepository.deleteAll();
    }
}