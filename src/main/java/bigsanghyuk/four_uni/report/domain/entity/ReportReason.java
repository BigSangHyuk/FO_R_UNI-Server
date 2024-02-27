package bigsanghyuk.four_uni.report.domain.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ReportReason {

    INSULTING("욕설_비하가_포함된_내용이에요"),
    ADVERTISING("상업적_광고가_포함된_내용이에요"),
    SEXUALITY("선정적이에요"),
    PRETENDING("사칭_사기가_포함된_내용이에요"),
    SCAM("낚시성_도배성_댓글이에요"),
    CUSTOM("직접_입력");

    private String reason;

    ReportReason(String reason) {
        this.reason = reason;
    }

    @JsonValue
    public String getReason() {
        return reason;
    }
}
