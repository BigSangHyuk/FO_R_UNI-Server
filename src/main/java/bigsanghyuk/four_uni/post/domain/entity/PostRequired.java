package bigsanghyuk.four_uni.post.domain.entity;

import bigsanghyuk.four_uni.user.enums.CategoryType;

import java.time.LocalDate;

public interface PostRequired {

    Long getPostId();
    CategoryType getCategory();
    String getTitle();
    LocalDate getDeadline();
}
