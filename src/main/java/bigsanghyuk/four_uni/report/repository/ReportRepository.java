package bigsanghyuk.four_uni.report.repository;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.report.domain.entity.Report;
import bigsanghyuk.four_uni.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByUserAndComment(User user, Comment comment);

    Optional<Report> findByUserAndPost(User user, Post post);

    @Query("SELECT r FROM Report r " +
            "WHERE r.user = :user AND r.post = :post AND r.deadline IS NOT NULL")
    Optional<Report> findDeadlineReport(@Param("user") User user, @Param("post") Post post);
}
