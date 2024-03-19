package bigsanghyuk.four_uni.exception.comment;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class CommentRemoveOtherUserException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "다른 유저의 댓글입니다.";

    public CommentRemoveOtherUserException() {
        super(message);
        this.status = StatusEnum.RESTRICTED;
    }

}