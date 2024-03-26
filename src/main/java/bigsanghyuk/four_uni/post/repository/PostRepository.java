package bigsanghyuk.four_uni.post.repository;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.PostRequired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Boolean existsPostByNoticeUrl(String noticeUrl);

    //categoryId가 서로 다른 글들을 동시에 조회 (필터에서 사용)
    List<Post> findPostByCategoryTypeIn(List<String> categoryNames);

    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category_id as categoryId, title, deadline FROM Posts " +
                    "WHERE is_classified = FALSE " +
                    "ORDER BY created_at DESC")
    List<PostRequired> findRequiredIsClassifiedFalse();

    //해당 연, 월을 기준으로 post 의 deadline 에 해당하는 글만 반환, 전 후 한달까지 포함
    @Query("SELECT p FROM Post p " +
            "WHERE ((YEAR(p.deadline) = :year AND MONTH(p.deadline) = :month) " +
            "OR (YEAR(p.deadline) = :prevYear AND MONTH(p.deadline) = :prevMonth) " +
            "OR (YEAR(p.deadline) = :nextYear AND MONTH(p.deadline) = :nextMonth)) " +
            "AND p.isClassified = TRUE " +
            "ORDER BY p.deadline")
    List<Post> findPostsByCurrentAndAdjacentMonths(
            @Param("year") int year,
            @Param("month") int month,
            @Param("prevYear") int prevYear,
            @Param("prevMonth") int prevMonth,
            @Param("nextYear") int nextYear,
            @Param("nextMonth") int nextMonth
    );

    @Query(nativeQuery = true,
            value = "SELECT post_id as postId, category_id as categoryId, title, deadline FROM Posts " +
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
}
