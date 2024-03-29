package bigsanghyuk.four_uni.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCommentInfo {

    private Long postId;
    private String content;
    private Long parentCommentId;
}
