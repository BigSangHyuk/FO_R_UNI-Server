package bigsanghyuk.four_uni.exception.user;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class PasswordMismatchException extends RuntimeException {

    private final StatusEnum status;

    private static final String message = "이전 비밀번호와 일치하지 않습니다.";

    public PasswordMismatchException() {
        super(message);
        this.status = StatusEnum.PASSWORD_MISMATCH;
    }
}
