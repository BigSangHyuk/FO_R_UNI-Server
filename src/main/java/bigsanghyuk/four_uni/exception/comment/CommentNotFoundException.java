package bigsanghyuk.four_uni.exception.comment;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class CommentNotFoundException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "해당 댓글이 더 이상 존재하지 않습니다.";

    public CommentNotFoundException() {
        super(message);
        this.status = StatusEnum.COMMENT_NOT_FOUND;
    }

}
