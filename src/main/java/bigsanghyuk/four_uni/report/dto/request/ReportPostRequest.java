package bigsanghyuk.four_uni.report.dto.request;

import bigsanghyuk.four_uni.report.domain.ReportPostInfo;
import bigsanghyuk.four_uni.report.domain.entity.ReportReason;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReportPostRequest {

    @NotNull
    private Long postId;
    @NotNull
    private ReportReason reason;
    private String detail;

    public ReportPostInfo toDomain() {
        return new ReportPostInfo(postId, reason, detail);
    }
}
