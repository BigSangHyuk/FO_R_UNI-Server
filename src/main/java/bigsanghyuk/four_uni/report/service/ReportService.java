package bigsanghyuk.four_uni.report.service;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.exception.comment.CommentNotFoundException;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.exception.report.ReportReasonNotFoundException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
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

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final ReportRepository reportRepository;

    public void reportComment(ReportCommentInfo reportCommentInfo) {

        Long commentId = reportCommentInfo.getCommentId();
        Long userId = reportCommentInfo.getUserId();
        ReportReason reason = reportCommentInfo.getReason();
        String detail = reportCommentInfo.getDetail();

        Comment comment = commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (reason == null) {
            throw new ReportReasonNotFoundException();
        }

        Optional<Report> report = reportRepository.findByUserIdAndCommentId(user.getId(), comment.getId());

        if (report.isPresent()) {
            Report existingReport = report.get();
            existingReport.setReason(reason);
            existingReport.setDetail(detail);
            reportRepository.save(existingReport);
        } else {
            comment.setReported(true);
            comment.setCommentReportCount(comment.getCommentReportCount() + 1);
            commentRepository.save(comment);

            Report newReport = Report.builder()
                    .userId(user.getId())
                    .commentId(comment.getId())
                    .reason(reason)
                    .detail(detail)
                    .build();

            reportRepository.save(newReport);
        }
    }

    public void reportPost(ReportPostInfo reportPostInfo) {

        Long postId = reportPostInfo.getPostId();
        Long userId = reportPostInfo.getUserId();
        ReportReason reason = reportPostInfo.getReason();
        String detail = reportPostInfo.getDetail();

        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

        if (reason == null) {
            throw new ReportReasonNotFoundException();
        }

        Optional<Report> report = reportRepository.findByUserIdAndPostId(user.getId(), post.getId());

        if (report.isPresent()) {
            Report existingReport = report.get();
            existingReport.setReason(reason);
            existingReport.setDetail(detail);
            reportRepository.save(existingReport);
        } else {
            post.setReported(true);
            post.setPostReportCount(post.getPostReportCount() + 1);
            postRepository.save(post);

            Report newReport = Report.builder()
                    .userId(user.getId())
                    .postId(post.getId())
                    .reason(reason)
                    .detail(detail)
                    .build();

            reportRepository.save(newReport);
        }
    }
}
