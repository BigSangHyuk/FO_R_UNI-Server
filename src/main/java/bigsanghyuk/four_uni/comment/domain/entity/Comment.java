package bigsanghyuk.four_uni.comment.domain.entity;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
import bigsanghyuk.four_uni.user.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

@Entity @Builder
@Getter @Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE comments SET deleted = TRUE WHERE comment_id = ?") // soft delete 사용
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "post_id")
    private Long postId;

    @Setter @ColumnDefault("false")
    private boolean reported;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ColumnDefault("0")
    private int commentLike;
    private String content;
    @ColumnDefault("0")
    @Setter
    private int commentReportCount;

    @ColumnDefault("false")
    private boolean deleted;

    public void edit(@Valid EditCommentInfo editCommentInfo) {
        this.content = editCommentInfo.getContent();
    }
}
