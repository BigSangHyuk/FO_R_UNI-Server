package bigsanghyuk.four_uni.comment.dto.request;

import bigsanghyuk.four_uni.report.domain.entity.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportCommentRequest {

    private Long userId;
    private Long commentId;
    private ReportReason reason;
}
