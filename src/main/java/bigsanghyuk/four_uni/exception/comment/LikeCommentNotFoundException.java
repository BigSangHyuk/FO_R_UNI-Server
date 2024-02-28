package bigsanghyuk.four_uni.exception.comment;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class LikeCommentNotFoundException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "좋아요 한 댓글을 찾을 수 없습니다.";

    public LikeCommentNotFoundException() {
        super(message);
        this.status = StatusEnum.COMMENT_NOT_FOUND;
    }

}
