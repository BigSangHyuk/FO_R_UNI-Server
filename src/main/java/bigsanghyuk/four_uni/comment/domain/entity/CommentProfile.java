package bigsanghyuk.four_uni.comment.domain.entity;

import bigsanghyuk.four_uni.user.domain.entity.UserRequired;

public interface CommentProfile {

    Long getCommentId();

    Long getUserId();

    UserRequired getUser();

    Long getPostId();

    int getCommentLike();

    String getContent();
}
