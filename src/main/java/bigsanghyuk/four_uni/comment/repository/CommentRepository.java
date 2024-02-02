package bigsanghyuk.four_uni.comment.repository;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteCommentByPostIdAndId(Long postId, Long commentId);

    //@Transactional
    @Query("UPDATE Comment c SET c.commentLike = c.commentLike + 1 WHERE c.id = :commentId")
    void incrementLikesByCommentId(@Param("commentId") Long commentId);

    Optional<Comment> findByUserId(Long userId);
}
