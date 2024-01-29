package bigsanghyuk.four_uni.exception.post;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class PostNotFoundException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "해당 게시글이 더 이상 존재하지 않습니다. 다시 한번 확인하세요.";

    public PostNotFoundException() {
        super(message);
        this.status = StatusEnum.POST_NOT_FOUND;
    }

}
