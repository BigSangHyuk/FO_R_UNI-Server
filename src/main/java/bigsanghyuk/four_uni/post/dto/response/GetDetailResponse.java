package bigsanghyuk.four_uni.post.dto.response;

import bigsanghyuk.four_uni.user.enums.CategoryType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDetailResponse {

    private Long id;
    private CategoryType categoryType;
    private String title;
    private String content;
    private List<String> imageUrl;
    private int views;
    private boolean isClassified;
    private LocalDate postedAt;
    private LocalDate deadline;
    private String noticeUrl;
    private boolean reported;
    private int postReportCount;
}
