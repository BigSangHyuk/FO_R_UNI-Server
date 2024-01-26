package bigsanghyuk.four_uni.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LikedComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "liked_comment_id")
    private Long id;

    private Long userId;
    private Long commentId;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
