package bigsanghyuk.four_uni.comment.repository;

import bigsanghyuk.four_uni.comment.domain.entity.LikeComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    Optional<LikeComment> findByUserIdAndCommentId(Long userId, Long commentId);

    void deleteLikeCommentByCommentId(Long commentId);

    void deleteLikeCommentByUserIdAndCommentId(Long userId, Long commentId);

    List<LikeComment> findByUserIdOrderByIdDesc(Long userId);
}
