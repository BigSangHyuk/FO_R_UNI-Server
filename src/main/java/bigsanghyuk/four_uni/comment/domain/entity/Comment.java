package bigsanghyuk.four_uni.comment.domain.entity;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity @Builder
@Getter @Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private Long userId;
    private Long postId;
    @Setter @ColumnDefault("false")
    private boolean reported;
    @ColumnDefault("null")
    private Long parentCommentId;
    @ColumnDefault("0")
    private int commentLike;
    private String content;
    @ColumnDefault("0")
    @Setter
    private int commentReportCount;

    public Comment(Long userId, Long postId, Long parentCommentId, int commentLike, String content, boolean reported) {
        this.userId = userId;
        this.postId = postId;
        this.parentCommentId = parentCommentId;
        this.commentLike = commentLike;
        this.content = content;
        this.reported = reported;
    }

    public void edit(@Valid EditCommentInfo editCommentInfo) {
        this.content = editCommentInfo.getContent();
    }

    public void updateParent(Long parentCommentId) {
        this.parentCommentId = parentCommentId;
    }
}
