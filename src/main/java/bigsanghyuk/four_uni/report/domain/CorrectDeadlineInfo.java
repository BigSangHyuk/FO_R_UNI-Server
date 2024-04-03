package bigsanghyuk.four_uni.report.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CorrectDeadlineInfo {

    private Long postId;
    private LocalDate deadline;
}
