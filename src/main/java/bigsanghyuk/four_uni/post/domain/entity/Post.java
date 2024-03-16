package bigsanghyuk.four_uni.post.domain.entity;

import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private Long categoryId;
    @Setter @ColumnDefault("false")
    private boolean reported;
    private String title;
    private String content;
    private String imageUrl; // 이미지로 된 게시글일 때 : 이미지 URL
    private int views; // 조회수
    @Setter
    private int postReportCount;
    private boolean isClassified; // 미분류

    private LocalDate postedAt; //원글 게시일 (연/월/일만 포함되도 상관없을 것 같아서)
    private LocalDate deadline; //원글의 이벤트 마감일시

    public Post(Long categoryId, boolean reported, String title, String content, String imageUrl, int views, int postReportCount, boolean isClassified, LocalDate postedAt, LocalDate deadline) {
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