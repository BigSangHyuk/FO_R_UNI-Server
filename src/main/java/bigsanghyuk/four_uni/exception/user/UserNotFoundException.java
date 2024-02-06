package bigsanghyuk.four_uni.exception.user;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class UserNotFoundException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "해당 유저가 더 이상 존재하지 않습니다.";

    public UserNotFoundException() {
        super(message);
        this.status = StatusEnum.USER_NOT_FOUND;
    }

}
