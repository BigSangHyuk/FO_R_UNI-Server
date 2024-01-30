package bigsanghyuk.four_uni.report.service;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.exception.ReportReason;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.report.domain.entity.Report;
import bigsanghyuk.four_uni.comment.dto.request.ReportCommentRequest;
import bigsanghyuk.four_uni.report.repository.ReportRepository;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    private final ReportRepository reportRepository;

    public ReportCommentRequest reportComment(Long id, Long userId, String reportReasonString) {

        try {
            Comment comment = commentRepository.findById(id).orElse(null);
            User user = userRepository.findById(userId).orElse(null);

            ReportReason reportReason = ReportReason.valueOf(reportReasonString);

            Optional<Report> report = reportRepository.findByUserAndComment(user.getId(), comment.getId());

            if (report.isPresent()) {
                report.get().setReason(reportReason);
                reportRepository.save(report.get());
            } else {
                comment.setCommentReportCount(comment.getCommentReportCount() + 1);
                comment.setReportReason(reportReason);
                comment.setReportedBy(user);
                commentRepository.save(comment);

                Report newReport = new Report();
                newReport.setUserId(user.getId());
                newReport.setCommentId(comment.getId());
                newReport.setReason(reportReason);
                reportRepository.save(newReport);
            }

            return comment.toDto();

        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("댓글이 성공적으로 신고되었습니다!");
        return null;

    }

}
