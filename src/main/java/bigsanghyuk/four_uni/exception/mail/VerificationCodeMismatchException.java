package bigsanghyuk.four_uni.exception.mail;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class VerificationCodeMismatchException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "코드가 일치하지 않습니다.";

    public VerificationCodeMismatchException() {
        super(message);
        this.status = StatusEnum.VERIFICATION_MISMATCH;
    }

}
