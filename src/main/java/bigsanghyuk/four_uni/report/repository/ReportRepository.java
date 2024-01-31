package bigsanghyuk.four_uni.report.repository;

import bigsanghyuk.four_uni.report.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {

    Optional<Report> findByUserIdAndCommentId(Long userId, Long commentId);
}
