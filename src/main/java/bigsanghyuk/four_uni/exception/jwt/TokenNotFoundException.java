package bigsanghyuk.four_uni.exception.jwt;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class TokenNotFoundException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "토큰을 발견할 수 없습니다.";

    public TokenNotFoundException() {
        super(message);
        this.status = StatusEnum.TOKEN_NOT_FOUND;
    }

}
