package bigsanghyuk.four_uni.report.service;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.exception.report.ReportReasonNotFoundException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.report.domain.CorrectDeadlineInfo;
import bigsanghyuk.four_uni.report.domain.ReportCommentInfo;
import bigsanghyuk.four_uni.report.domain.ReportPostInfo;
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

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

    public boolean reportComment(Long userId, ReportCommentInfo reportCommentInfo) {

        Long commentId = reportCommentInfo.getCommentId();
        ReportReason reason = reportCommentInfo.getReason();
        String detail = reportCommentInfo.getDetail();

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (reason == null) {
            throw new ReportReasonNotFoundException();
        }
        if (comment.isDeleted()) {
            throw new CommentNotFoundException();
        }

        Optional<Report> report = reportRepository.findByUserAndComment(user, comment);
        Report savedReport;

        if (report.isPresent()) {
            Report updatedReport = editReport(report.get(), reason, detail);
            savedReport = reportRepository.save(updatedReport);
        } else {
            comment.setReported(true);
            comment.setCommentReportCount(comment.getCommentReportCount() + 1);
            commentRepository.save(comment);

            Report newReport = makeReport(user, comment, null, reason, detail);
            savedReport = reportRepository.save(newReport);
        }
        return savedReport.getId() != null;
    }

    public boolean reportPost(Long userId, ReportPostInfo reportPostInfo) {

        Long postId = reportPostInfo.getPostId();
        ReportReason reason = reportPostInfo.getReason();
        String detail = reportPostInfo.getDetail();

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (reason == null) {
            throw new ReportReasonNotFoundException();
        }

        Optional<Report> report = reportRepository.findByUserAndPost(user, post);
        Report savedReport;

        if (report.isPresent()) {
            Report updatedReport = editReport(report.get(), reason, detail);
            savedReport = reportRepository.save(updatedReport);
        } else {
            post.setReported(true);
            post.setPostReportCount(post.getPostReportCount() + 1);
            postRepository.save(post);

            Report newReport = makeReport(user, null, post, reason, detail);
            savedReport = reportRepository.save(newReport);
        }
        return savedReport.getId() != null;
    }

    public boolean reportDeadline(Long userId, CorrectDeadlineInfo correctDeadlineInfo) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(correctDeadlineInfo.getPostId()).orElseThrow(PostNotFoundException::new);
        LocalDate deadline = correctDeadlineInfo.getDeadline();

        Optional<Report> report = reportRepository.findDeadlineReport(user, post);
        Report savedReport;

        if (report.isPresent()) {
            Report updatedReport = editReport(report.get(), deadline);
            savedReport = reportRepository.save(updatedReport);
        } else {
            post.setReported(true);
            post.setPostReportCount(post.getPostReportCount() + 1);
            postRepository.save(post);

            Report newReport = makeReport(user, post, deadline);
            savedReport = reportRepository.save(newReport);
        }
        return savedReport.getId() != null;
    }

    private Report makeReport(User user, Comment comment, Post post, ReportReason reason, String detail) {
        return Report.builder()
                .user(user)
                .comment(comment)
                .post(post)
                .reason(reason)
                .detail(detail)
                .build();
    }

    private Report makeReport(User user, Post post, LocalDate deadline) {
        return Report.builder()
                .user(user)
                .post(post)
                .deadline(deadline)
                .build();
    }

    private Report editReport(Report report, ReportReason reason, String detail) {
        report.editReason(reason);
        report.editDetail(detail);
        return report;
    }

    private Report editReport(Report report, LocalDate deadline) {
        report.editDeadline(deadline);
        return report;
    }
}
