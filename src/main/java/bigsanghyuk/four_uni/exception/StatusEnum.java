package bigsanghyuk.four_uni.exception;

import lombok.Getter;

@Getter
public enum StatusEnum {

    USER_NOT_FOUND(404,"USER_NOT_FOUND"),
    EMAIL_DUPLICATE(403, "EMAIL_DUPLICATE"),
    POST_NOT_FOUND(404,"POST_NOT_FOUND"),
    COMMENT_NOT_FOUND(404,"COMMENT_NOT_FOUND"),
    ALREADY_LIKE(403, "ALREADY_LIKE"),
    REASON_NOT_FOUND(404, "REASON_NOT_FOUND"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    TOKEN_NOT_FOUND(404, "TOKEN_NOT_FOUND"),
    RESTRICTED(403, "RESTRICTED");

    private final int statusCode;
    private final String code;

    StatusEnum(int statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }
}