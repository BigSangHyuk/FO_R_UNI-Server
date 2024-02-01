package bigsanghyuk.four_uni.exception;

public enum StatusEnum {

    USER_NOT_FOUND(404,"USER_NOT_FOUND"),
    POST_NOT_FOUND(200,"POST_NOT_FOUND"),
    COMMENT_NOT_FOUND(404,"COMMENT_NOT_FOUND"),
    BAD_REQUEST(400, "BAD_REQUEST");

    private final int statusCode;
    private final String code;

    private StatusEnum(int statusCode, String code) {
        this.statusCode = statusCode;
        this.code = code;
    }
}