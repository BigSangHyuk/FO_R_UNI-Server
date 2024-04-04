package bigsanghyuk.four_uni.exception.user;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class WrongPasswordException extends RuntimeException {

    private final StatusEnum status;

    private static final String message = "비밀번호가 일치하지 않습니다.";

    public WrongPasswordException() {
        super(message);
        this.status = StatusEnum.PASSWORD_INVALID;
    }
}
