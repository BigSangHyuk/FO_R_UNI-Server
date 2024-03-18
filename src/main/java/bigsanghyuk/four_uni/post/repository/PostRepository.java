package bigsanghyuk.four_uni.post.repository;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.PostRequired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    //제목이나 내용에 키워드가 포함된 글 조회
    List<Post> findByTitleContainingOrContentContaining(String keywordTitle, String keywordContent);

    //categoryId가 서로 다른 글들을 동시에 조회 (필터에서 사용)
    List<Post> findByCategoryIdIn(List<Long> categoryIds);

    //startDate ~ endDate 사이에 게시된 글들 조회
    List<Post> findByPostedAtBetween(LocalDate startDate, LocalDate endDate);

    //deadline 이 특정 날짜 이전인 글만 조회
    List<Post> findByDeadlineBefore(LocalDate deadline);

    //미분류 게시판 사용 용도 (미분류된 게시글들만 조회)
    List<Post> findByIsClassifiedFalse();

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
}
