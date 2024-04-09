package bigsanghyuk.four_uni.exception.mail;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class VerificationCodeExpiredException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "만료된 코드입니다.";

    public VerificationCodeExpiredException() {
        super(message);
        this.status = StatusEnum.VERIFICATION_EXPIRED;
    }

}
