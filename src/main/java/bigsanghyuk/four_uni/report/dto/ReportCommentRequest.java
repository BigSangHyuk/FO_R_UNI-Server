package bigsanghyuk.four_uni.report.dto;

import bigsanghyuk.four_uni.report.domain.entity.ReportReason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportCommentRequest {

    @NotNull
    private Long userId;
    @NotNull
    private Long commentId;
    @NotNull
    private ReportReason reason;
    private String detail;
}
