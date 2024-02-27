package bigsanghyuk.four_uni.post.domain.entity;

import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
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
public class Scrapped extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "scrapped_id")
    private Long id;

    private Long userId;
    private Long postId;
    private Long categoryId;
    private LocalDateTime scrappedAt;

    public Scrapped(Long userId, Long postId, Long categoryId) {
        this.userId = userId;
        this.postId = postId;
        this.categoryId = categoryId;
        this.scrappedAt = LocalDateTime.now();  //scrap 해제 후 다시 scrap 상황 생각시
    }
}
