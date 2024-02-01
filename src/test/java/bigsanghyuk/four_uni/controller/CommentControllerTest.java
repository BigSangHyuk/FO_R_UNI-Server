package bigsanghyuk.four_uni.controller;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.comment.service.CommentService;
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
public class CommentControllerTest {

    Comment comment;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    RegisterCommentInfo registerCommentInfo;
    EditCommentInfo editCommentInfo;

    @BeforeEach // 테스트 실행할때마다 수행
    public void registerSetUp() {
        // given : 댓글 컨트롤러 테스트를 위한 초기 설정
        String randomCode = RandomStringUtils.randomAlphanumeric(15);

        Random random = new Random();
        Long randomNum = random.nextLong();
        comment = Comment.builder()
                .id(randomNum)
                .userId(1L)
                .postId(2L)
                .parentCommentId(3L)
                .commentLike(0)
                .content(randomCode)
                .commentReportCount(0)
                .createdAt(LocalDateTime.now())
                .build();
    }

    @AfterEach
    void afterEach() {
        // 테스트 종료 후 데이터 정리
        commentRepository.deleteAll();
    }

    @Test
    @DisplayName("댓글 등록")
    void registerComment() {
        // when : 댓글 등록 동작 수행
        Comment savedComment = commentRepository.save(comment);

        // then : 댓글 등록 후에는 ID가 존재해야 하고 저장된 댓글 정보를 조회하여 존재해야 함
        assertNotNull(savedComment.getId());

        Comment retrievedComment = commentRepository.findById(savedComment.getId()).orElse(null);
        assertNotNull(retrievedComment);
        assertEquals(comment.getUserId(), retrievedComment.getUserId());
        assertEquals(comment.getContent(), retrievedComment.getContent());
    }

    @Test
    @DisplayName("댓글 수정")
    void editComment() {
        // given : 댓글 등록 및 수정할 내용을 set
        Comment savedComment = commentRepository.save(comment);

        editCommentInfo = new EditCommentInfo();

        editCommentInfo.setUserId(savedComment.getUserId());
        editCommentInfo.setContent(savedComment.getContent());

        // and : 수정할 정보
        String updatedContent = "updated_content";

        // when : 댓글 수정 동작 수행
        editCommentInfo.setContent(updatedContent);
        Comment updatedComment = commentService.edit(savedComment.getId(), editCommentInfo);

        // then : 업데이트된 댓글 정보 확인
        assertNotNull(updatedComment);
        assertEquals(savedComment.getId(), updatedComment.getId());
        assertEquals(updatedContent, updatedComment.getContent());
    }

}
