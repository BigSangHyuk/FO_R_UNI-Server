package bigsanghyuk.four_uni.exception.user;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class EmailDuplicateException extends RuntimeException{

    private final StatusEnum status;

    private static final String message = "이미 존재하는 이메일입니다.";

    public EmailDuplicateException() {
        super(message);
        this.status = StatusEnum.EMAIL_DUPLICATE;
    }
}
