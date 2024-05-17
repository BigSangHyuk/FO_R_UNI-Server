package bigsanghyuk.four_uni.post.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GetRequiredResponse {

    private Long postId;
    private String category;
    private String title;
    private String content;
    private LocalDate deadline;
    private LocalDate postedAt;
}
