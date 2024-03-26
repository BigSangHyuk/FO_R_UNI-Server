package bigsanghyuk.four_uni.post.repository;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.Scrapped;
import bigsanghyuk.four_uni.post.domain.entity.ScrappedRequired;
import bigsanghyuk.four_uni.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScrappedRepository extends JpaRepository<Scrapped, Long> {

    @Transactional
    void deleteScrappedByUserAndPost(User user, Post post);

    //각 user 마다의 scrapped 게시물 리스트를 스크랩된 순서로 반환
    @Query(
            nativeQuery = true,
            value = "SELECT post_id as postId FROM Scrapped " +
                    "WHERE user_id = :userId " +
                    "ORDER BY created_at DESC")
    List<ScrappedRequired> findRequired(@Param("userId") Long userId);
}

