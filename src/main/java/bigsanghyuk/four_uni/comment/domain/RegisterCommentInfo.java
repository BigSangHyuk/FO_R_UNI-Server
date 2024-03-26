package bigsanghyuk.four_uni.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterCommentInfo {

    private Long userId;
    private Long postId;
    private String content;
    private Long parentCommentId;
}
