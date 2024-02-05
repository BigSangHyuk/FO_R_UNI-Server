package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
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
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CommentServiceTest {

    Comment comment;
    Comment reply;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    EditCommentInfo editCommentInfo;

    @Test
    @DisplayName("댓글 등록")
    void addComment() {
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
    @DisplayName("댓글 수정 성공")
    void editCommentSuccess() {
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

    @Test
    @DisplayName("댓글 수정 실패 - 다른 유저")
    void editCommentOthers() {
        Comment savedComment = commentRepository.save(comment);

        editCommentInfo = new EditCommentInfo();
        editCommentInfo.setUserId(comment.getUserId() + 1L);
        editCommentInfo.setContent(savedComment.getContent());

        // and : 수정할 정보
        String updatedContent = "updated_content";

        // when : 댓글 수정 동작 수행
        editCommentInfo.setContent(updatedContent);

        // then : 업데이트된 댓글 정보 확인
        assertThrows(IllegalArgumentException.class, () -> commentService.edit(savedComment.getId(), editCommentInfo));
    }

    @Test
    @DisplayName("댓글 수정 실패 - 잘못된 댓글 id")
    void editCommentWrongId() {
        Comment savedComment = commentRepository.save(comment);

        editCommentInfo = new EditCommentInfo();
        editCommentInfo.setUserId(comment.getUserId());
        editCommentInfo.setContent(comment.getContent());

        // and : 수정할 정보
        String updatedContent = "updated_content";

        // when : 댓글 수정 동작 수행
        editCommentInfo.setContent(updatedContent);

        // then : 업데이트된 댓글 정보 확인
        assertThrows(IllegalArgumentException.class, () -> commentService.edit(savedComment.getId() + 1L, editCommentInfo));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void removeComment() {
        // given : 댓글 등록
        Comment savedComment = commentRepository.save(comment);

        // when : 댓글 삭제 동작 수행
        commentRepository.delete(savedComment);

        // then : 삭제된 댓글 정보 조회 시 null이 반환되어야 함
        Optional<Comment> deletedComment = commentRepository.findById(savedComment.getId());
        assertFalse(deletedComment.isPresent());
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 없는 글")
    void removeCommentNullPost() {
        // given : 댓글 등록
        Comment savedComment = commentRepository.save(comment);

        // when : 잘못된 postId
        long wrongPostId = savedComment.getPostId() + 100L;

        // then: 잘못된 postId 입력 시 IllegalArgumentException 이 던져져야 함
        assertThrows(IllegalArgumentException.class, () -> commentService.remove(wrongPostId, savedComment.getId()));
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 없는 댓글")
    void removeCommentNullComment() {
        // given : 댓글 등록
        Comment savedComment = commentRepository.save(comment);

        // when : 잘못된 commentId
        long wrongCommentId = savedComment.getId() + 100L;

        // then: 잘못된 commentId 입력 시 IllegalArgumentException 이 던져져야 함
        assertThrows(IllegalArgumentException.class, () -> commentService.remove(savedComment.getPostId(), wrongCommentId));
    }

    @Test
    @DisplayName("대댓글 등록")
    void replyComment() {
        commentRepository.save(comment);
        commentRepository.save(reply);

        Comment motherComment = commentRepository.findByUserId(comment.getUserId()).get();
        Comment childComment = commentRepository.findByUserId(reply.getUserId()).get();

        // then : 댓글 등록 후에는 ID가 존재해야 하고 저장된 댓글 정보를 조회하여 존재해야 함
        assertNotNull(motherComment.getId());

        childComment.updateParent(motherComment.getId());

        assertEquals(childComment.getParentCommentId(), motherComment.getId());
    }

    @Test
    @DisplayName("해당 글 전체 댓글 조회")
    void getAllCommentsByPostId() {
//        commentRepository.save(comment);
//        commentRepository.save(reply);
//
//        List<Comment> comments = commentService.getAllComments(comment.getPostId());
//
//
    }

    @BeforeEach
    public void registerSetUp() {
        // given : 댓글 컨트롤러 테스트를 위한 초기 설정
        String randomCode = RandomStringUtils.randomAlphanumeric(15);

        Random random = new Random();
        Long randomNum = random.nextLong();

        comment = Comment.builder()
                .id(100L)
                .userId(1L)
                .postId(2L)
                .parentCommentId(null)
                .commentLike(0)
                .content(randomCode)
                .commentReportCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        reply = Comment.builder()
                .id(new Random().nextLong())
                .userId(101L)
                .postId(2L)
                .parentCommentId(100L)
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
}