package bigsanghyuk.four_uni.report.domain;

import bigsanghyuk.four_uni.report.domain.entity.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportCommentInfo {

    private Long commentId;
    private ReportReason reason;
    private String detail;
}
