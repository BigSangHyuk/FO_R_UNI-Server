package bigsanghyuk.four_uni.comment.repository;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.domain.entity.CommentProfile;
import bigsanghyuk.four_uni.comment.domain.entity.CommentRequired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(nativeQuery = true,
            value = "SELECT post_id as postId FROM comments " +
                    "WHERE user_id = :userId AND deleted = FALSE " +
                    "ORDER BY comment_id DESC")
    List<CommentProfile> findCommentedPostId(@Param("userId") Long userId);

    @Query(nativeQuery = true,
            value = "SELECT comment_id as commentId, user_id as userId, post_id as postId, " +
                    "comment_like as commentLike, content as content FROM comments " +
                    "WHERE comment_id = :commentId AND deleted = false")
    CommentRequired findCommentRequired(@Param("commentId") Long commentId);

    @Query("SELECT c FROM Comment c WHERE c.postId = :postId AND c.parent IS NULL")
    List<Comment> findParents(@Param("postId") Long postId);

    List<Comment> findByParent(Comment parent);
}
