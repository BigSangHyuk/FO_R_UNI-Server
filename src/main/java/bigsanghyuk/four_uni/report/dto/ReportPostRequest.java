package bigsanghyuk.four_uni.report.dto;

import bigsanghyuk.four_uni.report.domain.entity.ReportReason;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportPostRequest {

    @NotNull
    private Long postId;
    @NotNull
    private Long userId;
    @NotNull
    private ReportReason reason;
    private String detail;

}
