package bigsanghyuk.four_uni.comment.dto.request;

import bigsanghyuk.four_uni.comment.domain.LikeCommentInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LikeCommentRequest {

    @NotNull
    private Long commentId;

    public LikeCommentInfo toDomain() {
        return new LikeCommentInfo(commentId);
    }

}
