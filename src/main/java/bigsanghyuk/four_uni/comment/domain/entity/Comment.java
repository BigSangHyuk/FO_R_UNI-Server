package bigsanghyuk.four_uni.comment.domain.entity;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private Long userId;
    private Long postId;
    private Long parentCommentId;
    private int commentLike;
    private String content;
    private int commentReportCount;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Comment(Long userId, String content) {
        this.userId = userId;
        this.content = content;
    }

    public void CommentEdit(@Valid EditCommentInfo editCommentInfo) {
        this.content = editCommentInfo.getContent();
    }


    public boolean CommentRemove(Long postId, Long commentId) {
        if (this.postId.equals(postId) && this.id.equals(commentId)) return true; // 게시글 id도 같고, 댓글의 id도 같으면 true
        return false; // 그렇지 않은 경우 false를 반환
    }
}
