package bigsanghyuk.four_uni.comment.dto.request;

import bigsanghyuk.four_uni.comment.domain.LikeCommentInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeCommentRequest {

    @NotNull
    private Long userId;

    @NotNull
    private Long commentId;

    public LikeCommentInfo toDomain() {
        return new LikeCommentInfo(userId, commentId);
    }

}
