package bigsanghyuk.four_uni.comment.dto.request;

import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EditCommentRequest {

    private Long postId;
    private Long commentId;
    private Long userId;
    private String content;

    public EditCommentInfo toDomain() {
        return new EditCommentInfo(postId, commentId, userId, content);
    }
}
