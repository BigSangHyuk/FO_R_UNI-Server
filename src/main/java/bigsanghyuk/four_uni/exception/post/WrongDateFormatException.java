package bigsanghyuk.four_uni.exception.post;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class WrongDateFormatException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "날짜 형식이 올바르지 않습니다.";

    public WrongDateFormatException() {
        super(message);
        this.status = StatusEnum.WRONG_DATE_FORMAT;
    }

}
