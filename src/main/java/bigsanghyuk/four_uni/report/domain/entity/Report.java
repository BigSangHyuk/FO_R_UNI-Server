package bigsanghyuk.four_uni.report.domain.entity;

import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity @Builder
@Getter @Setter
@Table(name = "reports")
@NoArgsConstructor
@AllArgsConstructor
public class Report extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // 신고하는 유저의 아이디

    private Long commentId;

    private Long postId;

    @Enumerated(EnumType.STRING)
    private ReportReason reason; // 신고 사유

    private String detail; // 상세 사유
}