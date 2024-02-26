package bigsanghyuk.four_uni.report.dto;

import bigsanghyuk.four_uni.report.domain.entity.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportPostRequest {

    private Long postId;
    private Long userId;
    private ReportReason reason;

}
