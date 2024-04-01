package bigsanghyuk.four_uni.comment.dto.request;

import bigsanghyuk.four_uni.comment.domain.DeleteCommentInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCommentRequest {

    private Long postId;
    private Long commentId;

    public DeleteCommentInfo toDomain() {
        return new DeleteCommentInfo(postId, commentId);
    }
}
