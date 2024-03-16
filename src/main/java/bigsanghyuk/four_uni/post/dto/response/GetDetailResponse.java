package bigsanghyuk.four_uni.post.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDetailResponse {

    private Long id;
    private Long categoryId;
    private String title;
    private String content;
    private String imageUrl;
    private int views;
    private boolean isClassified;
    private LocalDate postedAt;
    private LocalDate deadline;
}
