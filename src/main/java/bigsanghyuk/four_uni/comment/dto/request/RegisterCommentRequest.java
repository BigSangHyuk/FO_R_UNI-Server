package bigsanghyuk.four_uni.comment.dto.request;

import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCommentRequest {

    @NotNull
    private Long postId;

    private Long parentCommentId;

    @NotNull
    private String content;

    public RegisterCommentInfo toDomain() {
        return new RegisterCommentInfo(postId, content, parentCommentId);
    }

}
