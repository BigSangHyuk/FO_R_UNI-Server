package bigsanghyuk.four_uni.comment.domain.entity;

public interface CommentProfile {

    Long getCommentId();

    Long getUserId();

    Long getPostId();

    int getCommentLike();

    String getContent();

    boolean isDeleted();
}
