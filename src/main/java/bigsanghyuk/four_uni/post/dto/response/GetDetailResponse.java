package bigsanghyuk.four_uni.post.dto.response;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetDetailResponse {

    private Long id;
    private String category;
    private String title;
    private String content;
    private List<String> imageUrl;
    private int views;
    private boolean isClassified;
    private LocalDate postedAt;
    private LocalDate deadline;
    private String noticeUrl;
    private boolean reported;
    private int postReportCount;

    public GetDetailResponse(Post post) {
        this.id = post.getId();
        this.category = post.getCategoryType().getValue();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.imageUrl = post.getImageUrl();
        this.views = post.getViews();
        this.isClassified = post.isClassified();
        this.postedAt = post.getPostedAt();
        this.deadline = post.getDeadline();
        this.noticeUrl = post.getNoticeUrl();
        this.reported = post.isReported();
        this.postReportCount = post.getPostReportCount();
    }
}
