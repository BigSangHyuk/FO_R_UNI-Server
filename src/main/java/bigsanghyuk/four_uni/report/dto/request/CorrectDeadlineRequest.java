package bigsanghyuk.four_uni.report.dto.request;

import bigsanghyuk.four_uni.report.domain.CorrectDeadlineInfo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CorrectDeadlineRequest {

    private Long postId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate deadline;

    public CorrectDeadlineInfo toDomain() {
        return new CorrectDeadlineInfo(postId, deadline);
    }
}
