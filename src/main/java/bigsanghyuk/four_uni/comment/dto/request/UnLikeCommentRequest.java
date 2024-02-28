package bigsanghyuk.four_uni.comment.dto.request;

import bigsanghyuk.four_uni.comment.domain.UnLikeCommentInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UnLikeCommentRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long commentId;

    public UnLikeCommentInfo toDomain() {
        return new UnLikeCommentInfo(userId, commentId);
    }
}
