package bigsanghyuk.four_uni.comment.repository;

import bigsanghyuk.four_uni.comment.domain.entity.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    Optional<LikeComment> findByUserIdAndCommentId(Long userId, Long commentId);
}
