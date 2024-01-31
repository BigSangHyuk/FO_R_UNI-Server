package bigsanghyuk.four_uni.report.domain.entity;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.exception.ReportReason;
import bigsanghyuk.four_uni.user.domain.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Report {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // 신고하는 유저의 아이디

    private Long commentId;

    private Long postId;

    @Enumerated(EnumType.STRING)
    private ReportReason reason; // 신고 사유

//    private String detail; // 상세 사유

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

//    private Long targetId; // 신고되는 댓글 or 게시글의 id
//    private int targetType; // 댓글인지 게시글인지
}
