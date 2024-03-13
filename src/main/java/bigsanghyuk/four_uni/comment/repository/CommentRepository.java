package bigsanghyuk.four_uni.comment.repository;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteCommentByPostIdAndId(Long postId, Long commentId);

    @Transactional
    @Modifying
    @Query("UPDATE Comment c SET c.commentLike = c.commentLike + 1 WHERE c.id = :commentId")
    void increaseLikesByCommentId(@Param("commentId") Long commentId);

    @Transactional
    @Modifying
    @Query("UPDATE Comment c SET c.commentLike = c.commentLike - 1 WHERE c.id = :commentId")
    void decreaseLikesByCommentId(@Param("commentId") Long commentId);

    Optional<List<Comment>> findByUserIdOrderByIdDesc(Long userId);

    @Query("SELECT c FROM Comment c " +
            "WHERE c.postId = :postId AND c.parentCommentId = :parentCommentId AND c.parentCommentId IS NOT NULL " +
            "ORDER BY c.createdAt ASC")
    List<Comment> findChildComments(@Param("postId") Long postId, @Param("parentCommentId") Long parentCommentId);

    @Query("SELECT c FROM Comment c " +
            "WHERE c.postId = :postId AND c.parentCommentId IS NULL " +
            "ORDER BY c.createdAt ASC")
    List<Comment> findParentComments(@Param("postId") Long postId);
}
