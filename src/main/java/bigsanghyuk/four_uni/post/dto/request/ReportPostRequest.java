package bigsanghyuk.four_uni.post.dto.request;

import bigsanghyuk.four_uni.exception.ReportReason;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportPostRequest {

    private Long postId;
    private Long userId;
    private ReportReason reason;

}
