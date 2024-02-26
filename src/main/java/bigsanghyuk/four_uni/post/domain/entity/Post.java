package bigsanghyuk.four_uni.post.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private Long categoryId;
    private boolean report;
    private String title;
    private String content;
    private String imageUrl; // 이미지로 된 게시글일 때 : 이미지 URL
    private int views; // 조회수
    private int postReportCount;
    private boolean isClassified; // 미분류

    private LocalDate postedAt; //원글 게시일 (연/월/일만 포함되도 상관없을 것 같아서)
    private LocalDateTime deadline; //원글의 이벤트 마감일시

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Post(Long categoryId, boolean report, String title, String content, String imageUrl, int views, int postReportCount, boolean isClassified, LocalDate postedAt, LocalDateTime deadline) {
        this.categoryId = categoryId;
        this.report = report;
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
