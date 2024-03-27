package bigsanghyuk.four_uni.comment.repository;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.domain.entity.LikeComment;
import bigsanghyuk.four_uni.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {
    Optional<LikeComment> findByUserAndComment(User user, Comment comment);

    void deleteLikeCommentByComment(Comment comment);

    void deleteLikeCommentByUserAndComment(User user, Comment comment);

    @Query("SELECT c.comment.id FROM LikeComment c " +
            "WHERE c.user = :user " +
            "ORDER BY c.createdAt DESC")
    List<Long> findCommentIds(@Param("user") User user);
}
