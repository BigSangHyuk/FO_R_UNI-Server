package bigsanghyuk.four_uni.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ExceptionMessage {
    private StatusEnum status;
    private String message;

    public static ExceptionMessage of(StatusEnum status, String message) {
        return ExceptionMessage.builder().status(status)
                .message(message)
                .build();
    }

    public static ExceptionMessage of(String message) {
        return ExceptionMessage.of(StatusEnum.BAD_REQUEST, message);
    }
}