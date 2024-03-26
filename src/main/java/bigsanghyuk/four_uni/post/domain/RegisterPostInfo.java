package bigsanghyuk.four_uni.post.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPostInfo {

    private String title;
    private String content;
    private Long categoryId;
    private List<String> imageUrl;
    private String noticeUrl;
    private String postedAt;
    private String deadline;
    private Boolean isClassified;
}
