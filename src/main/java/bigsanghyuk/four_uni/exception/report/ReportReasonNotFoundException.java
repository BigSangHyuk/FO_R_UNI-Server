package bigsanghyuk.four_uni.exception.report;

import bigsanghyuk.four_uni.exception.StatusEnum;
import lombok.Getter;

@Getter
public class ReportReasonNotFoundException extends IllegalArgumentException {

    private final StatusEnum status;

    private static final String message = "해당 댓글이 더 이상 존재하지 않습니다.";

    public ReportReasonNotFoundException() {
        super(message);
        this.status = StatusEnum.REASON_NOT_FOUND;
    }
}
