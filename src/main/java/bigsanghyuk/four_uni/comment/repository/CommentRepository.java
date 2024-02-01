package bigsanghyuk.four_uni.comment.repository;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void deleteCommentByPostIdAndId(Long postId, Long commentId);

    Optional<Comment> findByUserId(Long userId);
}
