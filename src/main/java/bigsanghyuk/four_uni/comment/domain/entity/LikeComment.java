package bigsanghyuk.four_uni.comment.domain.entity;

import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity @Getter
@Table(name = "like_comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikeComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liked_comment_id")
    private Long id;

    private Long userId;
    private Long commentId;

    public LikeComment(Long userId, Long commentId) {
        this.userId = userId;
        this.commentId = commentId;
    }
}
