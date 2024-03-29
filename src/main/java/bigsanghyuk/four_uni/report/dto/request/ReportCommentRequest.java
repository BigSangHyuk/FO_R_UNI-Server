package bigsanghyuk.four_uni.report.dto.request;

import bigsanghyuk.four_uni.report.domain.ReportCommentInfo;
import bigsanghyuk.four_uni.report.domain.ReportPostInfo;
import bigsanghyuk.four_uni.report.domain.entity.ReportReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReportCommentRequest {

    @NotNull
    private Long commentId;
    @NotNull
    private ReportReason reason;
    private String detail;

    public ReportCommentInfo toDomain() {
        return new ReportCommentInfo(commentId, reason, detail);
    }
}
