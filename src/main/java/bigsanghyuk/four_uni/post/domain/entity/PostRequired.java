package bigsanghyuk.four_uni.post.domain.entity;

import bigsanghyuk.four_uni.user.enums.CategoryType;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDate;

@JsonInclude(JsonInclude.Include.NON_NULL)
public interface PostRequired {

    Long getPostId();
    CategoryType getCategory();
    String getTitle();
    String getContent();
    LocalDate getDeadline();
    LocalDate getPostedAt();
}
