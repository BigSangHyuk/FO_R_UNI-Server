package bigsanghyuk.four_uni.comment.repository;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

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
            "WHERE c.post = :postId AND c.parent = :parent AND c.parent IS NOT NULL " +
            "ORDER BY c.createdAt ASC")
    List<Comment> findChildComments(@Param("postId") Post post, @Param("parent") Comment parent);

    @Query("SELECT c FROM Comment c " +
            "WHERE c.post = :post AND c.parent IS NULL " +
            "ORDER BY c.createdAt ASC")
    List<Comment> findParentComments(@Param("post") Post post);

    List<Comment> findAllByIdIn(List<Long> commentIds);
}
