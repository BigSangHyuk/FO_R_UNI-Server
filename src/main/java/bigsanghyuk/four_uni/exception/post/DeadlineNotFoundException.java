package bigsanghyuk.four_uni.exception.post;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class DeadlineNotFoundException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "마감 기한이 존재하지 않습니다.";

    public DeadlineNotFoundException() {
        super(message);
        this.status = StatusEnum.DEADLINE_NOT_FOUND;
    }

}
