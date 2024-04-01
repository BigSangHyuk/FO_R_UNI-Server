package bigsanghyuk.four_uni.exception.scrapped;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class AlreadyScrappedException extends RuntimeException{

    private final StatusEnum status;

    private static final String message = "이미 스크랩한 글입니다.";

    public AlreadyScrappedException() {
        super(message);
        this.status = StatusEnum.ALREADY_SCRAPPED;
    }
}
