package bigsanghyuk.four_uni.comment.domain.entity;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.user.domain.entity.User;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity @Builder
@Getter @Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public void edit(@Valid EditCommentInfo editCommentInfo) {
        this.content = editCommentInfo.getContent();
    }
}
