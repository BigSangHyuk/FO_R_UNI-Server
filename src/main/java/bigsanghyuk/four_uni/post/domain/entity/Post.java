package bigsanghyuk.four_uni.post.domain.entity;

import bigsanghyuk.four_uni.config.StringToListConverter;
import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;
import java.util.List;

@Entity @Getter @Builder
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;
    private Long categoryId;
    @Setter @ColumnDefault("false")
    private boolean reported;
    private String title;
    @Column(columnDefinition = "LONGTEXT")
    private String content;
    @Convert(converter = StringToListConverter.class)
    private List<String> imageUrl; // 이미지로 된 게시글일 때 : 이미지 URL
    @ColumnDefault("0")
    private int views; // 조회수
    @Setter @ColumnDefault("0")
    private int postReportCount;
    private boolean isClassified; // 미분류

    private LocalDate postedAt; //원글 게시일 (연/월/일만 포함되도 상관없을 것 같아서)
    private LocalDate deadline; //원글의 이벤트 마감일시

    @Column(name = "notice_url", unique = true)
    private String noticeUrl;

    public Post(Long categoryId, boolean reported, String title, String content, List<String> imageUrl, int views, int postReportCount, boolean isClassified, LocalDate postedAt, LocalDate deadline) {
        this.categoryId = categoryId;
        this.reported = reported;
        this.title = title;
        this.content = content;
        this.imageUrl = imageUrl;
        this.views = views;
        this.postReportCount = postReportCount;
        this.isClassified = isClassified;
        this.postedAt = postedAt;
        this.deadline = deadline;
    }
}