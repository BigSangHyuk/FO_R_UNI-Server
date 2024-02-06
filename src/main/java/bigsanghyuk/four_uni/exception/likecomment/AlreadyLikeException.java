package bigsanghyuk.four_uni.exception.likecomment;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class AlreadyLikeException extends RuntimeException{

    private final StatusEnum status;

    private static final String message = "이미 좋아요 누른 댓글입니다.";

    public AlreadyLikeException() {
        super(message);
        this.status = StatusEnum.ALREADY_LIKE;
    }
}
