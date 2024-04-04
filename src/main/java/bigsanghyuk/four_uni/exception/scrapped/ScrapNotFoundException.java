package bigsanghyuk.four_uni.exception.scrapped;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class ScrapNotFoundException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "스크랩 정보를 찾을 수 없습니다.";

    public ScrapNotFoundException() {
        super(message);
        this.status = StatusEnum.SCRAP_NOT_FOUND;
    }

}
