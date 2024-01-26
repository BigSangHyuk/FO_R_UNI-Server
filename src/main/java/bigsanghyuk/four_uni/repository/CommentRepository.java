package bigsanghyuk.four_uni.repository;

import bigsanghyuk.four_uni.domain.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    void delete(Long postId, Long commentId);

}
