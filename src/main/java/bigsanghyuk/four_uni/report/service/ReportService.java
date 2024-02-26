package bigsanghyuk.four_uni.report.service;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.exception.report.ReportReasonNotFoundException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.report.domain.entity.ReportReason;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.report.domain.entity.Report;
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

    public void reportComment(Long commentId, Long userId, String reportReasonString, String detail) {

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (reportReasonString == null) {
            throw new ReportReasonNotFoundException();
        }

        ReportReason reportReason = ReportReason.valueOf(reportReasonString);

        Optional<Report> report = reportRepository.findByUserIdAndCommentId(user.getId(), comment.getId());

        if (report.isPresent()) {
            Report existingReport = report.get();
            existingReport.setReason(reportReason);
            reportRepository.save(existingReport);
        } else {
            comment.setCommentReportCount(comment.getCommentReportCount() + 1);
            commentRepository.save(comment);

            Report newReport = Report.builder()
                    .userId(user.getId())
                    .commentId(comment.getId())
                    .reason(reportReason)
                    .detail(detail)
                    .build();

            reportRepository.save(newReport);
        }
    }

    public void reportPost(Long postId, Long userId, String reportReasonString, String detail) {

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (reportReasonString == null) {
            throw new ReportReasonNotFoundException();
        }

        ReportReason reportReason = ReportReason.valueOf(reportReasonString);

        Optional<Report> report = reportRepository.findByUserIdAndPostId(user.getId(), post.getId());

        if (report.isPresent()) {
            Report existingReport = report.get();
            existingReport.setReason(reportReason);
            reportRepository.save(existingReport);
        } else {
            post.setPostReportCount(post.getPostReportCount() + 1);
            postRepository.save(post);

            Report newReport = Report.builder()
                    .userId(user.getId())
                    .postId(post.getId())
                    .reason(reportReason)
                    .detail(detail)
                    .build();

            reportRepository.save(newReport);
        }
    }
}
