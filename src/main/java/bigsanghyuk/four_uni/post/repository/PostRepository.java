package bigsanghyuk.four_uni.post.repository;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.PostRequired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Boolean existsPostByNoticeUrl(String noticeUrl);

    //categoryId가 서로 다른 글들을 동시에 조회 (필터에서 사용)
    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category, title, deadline FROM Posts " +
                    "WHERE category in :category"
    )
    List<PostRequired> findPostRequiredFiltered(@Param("category") List<String> categoryNames);

    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category, title, deadline FROM Posts " +
                    "WHERE is_classified = FALSE " +
                    "ORDER BY created_at DESC")
    List<PostRequired> findRequiredIsClassifiedFalse();

    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category, title, deadline FROM Posts " +
                    "WHERE ((YEAR(deadline) = :year AND MONTH(deadline) = :month) " +
                    "OR (YEAR(deadline) = :prevYear AND MONTH(deadline) = :prevMonth) " +
                    "OR (YEAR(deadline) = :nextYear AND MONTH(deadline) = :nextMonth)) " +
                    "AND is_classified = TRUE " +
                    "ORDER BY deadline")
    List<PostRequired> findRequiredByCurrentAndAdjacentMonths(
            @Param("year") int year,
            @Param("month") int month,
            @Param("prevYear") int prevYear,
            @Param("prevMonth") int prevMonth,
            @Param("nextYear") int nextYear,
            @Param("nextMonth") int nextMonth
    );

    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category, title, deadline FROM Posts " +
                    "WHERE post_id = :postId")
   PostRequired findRequiredByPostId(@Param("postId") Long postId);
}
