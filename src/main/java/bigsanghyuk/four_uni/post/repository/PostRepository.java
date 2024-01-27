package bigsanghyuk.four_uni.post.repository;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    /*//제목에 title 키워드를 포함한 글 조회
    List<Post> findByTitleContaining(String title);*/

    //제목이나 내용에 키워드가 포함된 글 조회
    List<Post> findByTitleContainingOrContentContaining(String keywordTitle, String keywordContent);

    /*//categoryId 하나인 경우 조회
    List<Post> findByCategoryId(Long categoryId);*/

    //categoryId가 서로 다른 글들을 동시에 조회 (필터에서 사용)
    List<Post> findByCategoryIdIn(List<Long> categoryIds);

    //startDate ~ endDate 사이에 게시된 글들 조회
    List<Post> findByPostedAtBetween(LocalDate startDate, LocalDate endDate);

    //deadline 이 특정 날짜 이전인 글만 조회
    List<Post> findByDeadlineBefore(LocalDateTime deadline);

    //미분류 게시판 사용 용도 (미분류된 게시글들만 조회)
    List<Post> findByIsClassifiedFalse();
}
