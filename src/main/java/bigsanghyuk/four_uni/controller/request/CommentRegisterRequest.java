package bigsanghyuk.four_uni.controller.request;

import bigsanghyuk.four_uni.domain.CommentRegisterInfo;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRegisterRequest {

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

    public CommentRegisterInfo toDomain() {
        return new CommentRegisterInfo(userId, content);
    }

}
