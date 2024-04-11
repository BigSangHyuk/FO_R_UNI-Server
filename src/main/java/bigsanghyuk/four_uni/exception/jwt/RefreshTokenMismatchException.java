package bigsanghyuk.four_uni.exception.jwt;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class RefreshTokenMismatchException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "토큰이 동일하지 않습니다.";

    public RefreshTokenMismatchException() {
        super(message);
        this.status = StatusEnum.REFRESH_TOKEN_MISMATCH;
    }

}
