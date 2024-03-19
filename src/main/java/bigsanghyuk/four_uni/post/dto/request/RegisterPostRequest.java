package bigsanghyuk.four_uni.post.dto.request;

import bigsanghyuk.four_uni.post.domain.RegisterPostInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterPostRequest {

    @JsonProperty(value = "category_id")
    private Long categoryId;
    private String title;
    private String content;
    @JsonProperty(value = "img_url")
    private List<String> imageUrl;
    @JsonProperty(value = "isclassified")
    private Boolean isClassified;
    @JsonProperty(value = "posted_at")
    private String postedAt;
    private String deadline;
    @JsonProperty(value = "notice_url")
    private String noticeUrl;

    public RegisterPostInfo toDomain() {
        return new RegisterPostInfo(title, content, categoryId, imageUrl, noticeUrl, postedAt, deadline, isClassified);
    }
}
