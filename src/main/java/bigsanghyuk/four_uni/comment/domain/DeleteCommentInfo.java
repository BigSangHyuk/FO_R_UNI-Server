package bigsanghyuk.four_uni.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeleteCommentInfo {

    private Long postId;
    private Long commentId;
}
