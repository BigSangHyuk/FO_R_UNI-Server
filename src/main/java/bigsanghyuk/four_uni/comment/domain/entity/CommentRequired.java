package bigsanghyuk.four_uni.comment.domain.entity;

public interface CommentRequired {

    Long getCommentId();
    Long getUserId();
    Long getPostId();
    int getCommentLike();
    String getContent();
}
