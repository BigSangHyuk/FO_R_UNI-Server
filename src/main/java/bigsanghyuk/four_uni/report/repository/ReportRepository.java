package bigsanghyuk.four_uni.report.repository;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.report.domain.entity.Report;
import bigsanghyuk.four_uni.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByUserAndComment(User user, Comment comment);

    Optional<Report> findByUserAndPost(User user, Post post);
}
