package bigsanghyuk.four_uni.post.domain.entity;

import java.time.LocalDate;

public interface PostRequired {

    Long getPostId();
    Long getCategoryId();
    String getTitle();
    LocalDate getDeadline();
}
