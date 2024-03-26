package bigsanghyuk.four_uni.post.domain;

import bigsanghyuk.four_uni.user.enums.CategoryType;
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
    private CategoryType categoryType;
    private List<String> imageUrl;
    private String noticeUrl;
    private String postedAt;
    private String deadline;
    private Boolean isClassified;
}
