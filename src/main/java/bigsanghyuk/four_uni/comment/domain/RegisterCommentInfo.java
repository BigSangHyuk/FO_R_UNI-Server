package bigsanghyuk.four_uni.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RegisterCommentInfo {

    private Long userId;
    private String content;

}
