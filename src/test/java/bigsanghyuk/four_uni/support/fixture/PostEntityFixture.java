package bigsanghyuk.four_uni.support.fixture;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.user.enums.CategoryType;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public enum PostEntityFixture {

    POST_ACADEMY(CategoryType.ACADEMY, false, "test_학사", "testcontent", Collections.singletonList("testImageUrl"), 0, 0, false, LocalDate.now(), LocalDate.now(), "testNoticeUrl"),
    POST_ISIS(CategoryType.ISIS, false, "test_컴퓨터공학부", "testcontent2", Collections.singletonList("testImageUrl2"), 0, 0, false, LocalDate.now(), LocalDate.now(), "testNoticeUrl2");

    private final CategoryType categoryType;
    private final boolean reported;
    private final String title;
    private final String content;
    private final List<String> imageUrl;
    private final int views;
    private final int postReportCount;
    private final boolean isClassified;
    private final LocalDate postedAt;
    private final LocalDate deadline;
    private final String noticeUrl;

    PostEntityFixture(CategoryType categoryType, boolean reported, String title, String content, List<String> imageUrl, int views, int postReportCount, boolean isClassified, LocalDate postedAt, LocalDate deadline, String noticeUrl) {
        this.categoryType = categoryType;
        this.reported = reported;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.views = views;
        this.postReportCount = postReportCount;
        this.isClassified = isClassified;
        this.postedAt = postedAt;
        this.deadline = deadline;
        this.noticeUrl = noticeUrl;
    }

    public Post PostEntity_생성(Long id) {
        return Post.builder()
                .id(id)
                .title(this.title)
                .content(this.content)
                .categoryType(this.categoryType)
                .imageUrl(this.imageUrl)
                .noticeUrl(this.noticeUrl)
                .postedAt(this.postedAt)
                .deadline(this.deadline)
                .isClassified(this.isClassified)
                .build();
    }

    public Post PostEntity_생성() {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .categoryType(this.categoryType)
                .imageUrl(this.imageUrl)
                .noticeUrl(this.noticeUrl)
                .postedAt(this.postedAt)
                .deadline(this.deadline)
                .isClassified(this.isClassified)
                .build();
    }
}
