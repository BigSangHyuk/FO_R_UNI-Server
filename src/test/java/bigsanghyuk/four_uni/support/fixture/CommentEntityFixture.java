package bigsanghyuk.four_uni.support.fixture;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.user.domain.entity.User;

public enum CommentEntityFixture {
    COMMENT_1(UserEntityFixture.USER_NORMAL.UserEntity_생성(), PostEntityFixture.POST_ISIS.PostEntity_생성().getId(), false, null, 0, "testCommentContent_컴퓨터공학부", 0, false),
    COMMENT_2(UserEntityFixture.USER_NORMAL.UserEntity_생성(), PostEntityFixture.POST_ACADEMY.PostEntity_생성().getId(), false, null, 0, "testCommentContent_학사", 0, false);

    private final User user;
    private final Long postId;
    private final boolean reported;
    private final Comment parent;
    private final int commentLike;
    private final String content;
    private final int commentReportCount;
    private final boolean deleted;

    CommentEntityFixture(User user, Long postId, boolean reported, Comment parent, int commentLike, String content, int commentReportCount, boolean deleted) {
        this.user = user;
        this.postId = postId;
        this.reported = reported;
        this.parent = parent;
        this.commentLike = commentLike;
        this.content = content;
        this.commentReportCount = commentReportCount;
        this.deleted = deleted;
    }

    public Comment commentEntity_생성(Long id) {
        return Comment.builder()
                .id(id)
                .postId(this.postId)
                .content(this.content)
                .parent(this.parent)
                .build();
    }
}
