package bigsanghyuk.four_uni.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UnLikeCommentInfo {

    private Long userId;
    private Long commentId;

}
