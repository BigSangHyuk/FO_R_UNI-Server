package bigsanghyuk.four_uni.post.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Scrapped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrapped_id")
    private Long id;

    private Long userId;
    private Long postId;
    private Long categoryId;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime scrappedAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public Scrapped(Long userId, Long postId, Long categoryId) {
        this.userId = userId;
        this.postId = postId;
        this.categoryId = categoryId;
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.scrappedAt = now;  //scrap 해제 후 다시 scrap 상황 생각시
        this.updatedAt = now;   //삭제 후 새로운 컬럼 등록할 것 같아 생성자에 설정
    }
}
