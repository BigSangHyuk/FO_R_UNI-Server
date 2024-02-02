package bigsanghyuk.four_uni.comment.dto.request;

import bigsanghyuk.four_uni.comment.domain.RegisterCommentInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterCommentRequest {

    @NotNull
    private Long userId;
    @NotNull
    private Long postId;

    private Long parentCommentId;
    @NotNull
    private int commentLike;
    @NotNull
    private String content;

    private Long commentReportId;

    public RegisterCommentInfo toDomain() {
        return new RegisterCommentInfo(userId, content);
    }

}
