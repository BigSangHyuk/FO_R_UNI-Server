package bigsanghyuk.four_uni.post.repository;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.PostRequired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Boolean existsPostByNoticeUrl(String noticeUrl);

    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category, title, deadline FROM posts " +
                    "WHERE category in :category " +
                    "AND YEAR(deadline) = :year AND MONTH(deadline) = :month " +
                    "AND is_classified = TRUE " +
                    "ORDER BY deadline"
    )
    List<PostRequired> findFiltered(@Param("category") List<String> categoryNames, @Param("year") int year, @Param("month") int month);

    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category, title, deadline FROM posts " +
                    "WHERE is_classified = FALSE " +
                    "AND category in :category " +
                    "ORDER BY created_at DESC")
    List<PostRequired> findRequiredIsClassifiedFalse(@Param("category") List<String> categoryNames);

    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category, title, deadline FROM posts " +
                    "WHERE post_id = :postId")
    PostRequired findRequiredByPostId(@Param("postId") Long postId);

    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category, title, SUBSTRING(content, 1, 100) as content, deadline FROM posts " +
                    "WHERE (title LIKE CONCAT('%', :keyword, '%') OR content LIKE CONCAT('%', :keyword, '%')) " +
                    "AND is_classified = :classified " +
                    "AND category in :category " +
                    "ORDER BY created_at DESC"
    )
    List<PostRequired> findRequiredByKeyword(@Param("keyword") String keyword, @Param("classified") Boolean classified, @Param("category") List<String> categoryNames);

    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category, title, SUBSTRING(content, 1, 100) as content, deadline FROM posts " +
                    "WHERE (title LIKE CONCAT('%', :keyword, '%') OR content LIKE CONCAT('%', :keyword, '%')) " +
                    "AND deadline IS NOT NULL AND category in :category " +
                    "ORDER BY created_at DESC"
    )   // 최신순 정렬
    List<PostRequired> findByKeywordLatest(@Param("keyword") String keyword, @Param("category") List<String> categoryNames);

    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId, category, title, deadline FROM posts " +
                    "WHERE (title LIKE CONCAT('%', :keyword, '%') OR content LIKE CONCAT('%', :keyword, '%')) " +
                    "AND deadline IS NOT NULL AND category in :category " +
                    "ORDER BY CASE WHEN deadline >= CURDATE() THEN 0 ELSE 1 END, deadline, created_at"
    )   // 조회일자 기준 마감기한 임박순으로 정렬하되, 마감기한이 남은 글들을 0번째 그룹, 이미 지난 글들을 1번째 그룹 -> 기간 남은 글들이 먼저 나오고 그 뒤에 마감된 글 표시
    List<PostRequired> findByKeywordDeadline(@Param("keyword") String keyword, @Param("category") List<String> categoryNames);
}
