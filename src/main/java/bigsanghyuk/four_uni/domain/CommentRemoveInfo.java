package bigsanghyuk.four_uni.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentRemoveInfo {

    private int postId;
    private int commentId;

}
