package bigsanghyuk.four_uni.report.domain.entity;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.user.domain.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Builder
@Getter
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
public class Report extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 신고하는 유저의 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;    // 신고당하는 댓글

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    private ReportReason reason; // 신고 사유

    private String detail; // 상세 사유

    private LocalDate deadline; // 수정 요청 받을때만 사용

    public void editReason(ReportReason reason) {
        this.reason = reason;
    }

    public void editDetail(String detail) {
        this.detail = detail;
    }

    public void editDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}