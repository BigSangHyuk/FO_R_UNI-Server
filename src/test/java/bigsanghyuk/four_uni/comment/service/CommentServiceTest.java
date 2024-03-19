package bigsanghyuk.four_uni.comment.service;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class CommentServiceTest {

    static Comment parent;
    static Comment child;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    PostRepository postRepository;
    EditCommentInfo editCommentInfo;

    @Test
    @DisplayName("댓글 등록")
    void addComment() {
        // when : 댓글 등록 동작 수행
        Comment savedComment = commentRepository.save(new Comment(10L, 10L, null, 0, "example", false));

        // then : 댓글 등록 후에는 ID가 존재해야 하고 저장된 댓글 정보를 조회하여 존재해야 함
        assertNotNull(savedComment.getId());

        Comment retrievedComment = commentRepository.findById(savedComment.getId()).orElse(null);
        assertNotNull(retrievedComment);
        assertEquals(savedComment.getUserId(), retrievedComment.getUserId());
        assertEquals(savedComment.getContent(), retrievedComment.getContent());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void editCommentSuccess() {
        // given : 댓글 등록 및 수정할 내용을 set
        Comment savedComment = commentRepository.save(new Comment(10L, 1L, null, 0, "example", false));

        editCommentInfo = new EditCommentInfo();

        editCommentInfo.setUserId(savedComment.getUserId());
        editCommentInfo.setContent(savedComment.getContent());

        // and : 수정할 정보
        String updatedContent = "updated_content";

        // when : 댓글 수정 동작 수행
        editCommentInfo.setContent(updatedContent);
        Comment updatedComment = commentService.edit(savedComment.getPostId(), savedComment.getId(), editCommentInfo);

        // then : 업데이트된 댓글 정보 확인
        assertNotNull(updatedComment);
        assertEquals(savedComment.getId(), updatedComment.getId());
        assertEquals(updatedContent, updatedComment.getContent());
    }

    @Test
    @DisplayName("댓글 수정 실패 - 다른 유저")
    void editCommentOthers() {
        Comment savedComment = commentRepository.save(new Comment(10L, 10L, null, 0, "example", false));

        editCommentInfo = new EditCommentInfo();
        editCommentInfo.setUserId(savedComment.getUserId() + 1L);
        editCommentInfo.setContent(savedComment.getContent());

        // and : 수정할 정보
        String updatedContent = "updated_content";

        // when : 댓글 수정 동작 수행
        editCommentInfo.setContent(updatedContent);

        // then : 업데이트된 댓글 정보 확인
        assertThrows(IllegalArgumentException.class, () -> commentService.edit(savedComment.getPostId(), savedComment.getId(), editCommentInfo));
    }

    @Test
    @DisplayName("댓글 수정 실패 - 잘못된 댓글 id")
    void editCommentWrongId() {
        Comment savedComment = commentRepository.save(new Comment(10L, 10L, null, 0, "example", false));

        editCommentInfo = new EditCommentInfo();

        editCommentInfo.setUserId(savedComment.getUserId());
        editCommentInfo.setContent(savedComment.getContent());

        // and : 수정할 정보
        String updatedContent = "updated_content";

        // when : 댓글 수정 동작 수행
        editCommentInfo.setContent(updatedContent);

        // then : 업데이트된 댓글 정보 확인
        assertThrows(IllegalArgumentException.class, () -> commentService.edit(savedComment.getPostId(), savedComment.getId() + 1L, editCommentInfo));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void removeComment() {
        // given : 댓글 등록
//        Comment savedComment = commentRepository.save(comment);
        Comment savedComment = commentRepository.save(new Comment(10L, 10L, null, 0, "example", false));

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
//        Comment savedComment = commentRepository.save(comment);
        Comment savedComment = commentRepository.save(new Comment(10L, 10L, null, 0, "example", false));

        // when : 잘못된 postId
        long wrongPostId = savedComment.getPostId() + 100L;

        // then: 잘못된 postId 입력 시 IllegalArgumentException 이 던져져야 함
        assertThrows(IllegalArgumentException.class, () -> commentService.remove(wrongPostId, savedComment.getId()));
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 없는 댓글")
    void removeCommentNullComment() {
        // given : 댓글 등록
        Comment savedComment = commentRepository.save(new Comment(10L, 10L, null, 0, "example", false));

        // when : 잘못된 commentId
        long wrongCommentId = savedComment.getId() + 100L;

        // then: 잘못된 commentId 입력 시 IllegalArgumentException 이 던져져야 함
        assertThrows(IllegalArgumentException.class, () -> commentService.remove(savedComment.getPostId(), wrongCommentId));
    }

    @Test
    @DisplayName("대댓글 등록")
    void replyComment() {
        Comment motherComment = commentRepository.findById(parent.getId()).get();
        Comment childComment = commentRepository.findById(child.getId()).get();

        // then : 댓글 등록 후에는 ID가 존재해야 하고 저장된 댓글 정보를 조회하여 존재해야 함
        assertNotNull(motherComment.getId());

        childComment.updateParent(motherComment.getId());

        assertEquals(childComment.getParentCommentId(), motherComment.getId());
    }

    @Test
    @DisplayName("해당 글 전체 댓글 조회")
    void getAllCommentsByPostId() {
        List<Comment> comments = commentService.getAllComments(parent.getPostId());

        for (Comment comment : comments) {
            log.info("commentId={}, userId={}", comment.getId(), comment.getUserId());
        }

    }

    @Test
    @DisplayName("commentRepository 테스트")
    void commentRepositoryTest() {
        List<Comment> children = commentRepository.findChildComments(1L, parent.getId());
        for (Comment comment : children) {
            log.info("commentId={}, userId={}", comment.getId(), comment.getUserId());
        }
        List<Comment> parents = commentRepository.findParentComments(1L);
        for (Comment comment : parents) {
            log.info("commentId={}, userId={}", comment.getId(), comment.getUserId());
        }
    }

    @BeforeEach
    void beforeEach() {
        postRepository.save(new Post(1L, false, "exampleTest", "testtest", List.of("test"), 150, 0, true, LocalDate.now(), LocalDate.now()));
        parent = commentRepository.save(new Comment(100L, 1L, null, 0, "contentParent", false));
        commentRepository.save(new Comment(200L, 1L, null, 0, "contentExample", false));
        child = commentRepository.save(new Comment(300L, 1L, parent.getId(), 0, "contentChild", false));
        commentRepository.save(new Comment(400L, 1L, parent.getId(), 0, "contentExample", false));
        commentRepository.save(new Comment(500L, 1L, parent.getId(), 0, "contentExample", false));
    }

    @AfterEach
    void afterEach() {
        // 테스트 종료 후 데이터 정리
        commentRepository.deleteAll();
    }
}