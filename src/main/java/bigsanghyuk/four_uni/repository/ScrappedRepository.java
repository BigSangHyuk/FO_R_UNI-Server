package bigsanghyuk.four_uni.repository;

import bigsanghyuk.four_uni.domain.entity.Scrapped;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ScrappedRepository extends JpaRepository<Scrapped, Long> {

    //각 user 마다의 scrapped 게시물 리스트를 반환
    List<Scrapped> findByUserId(Long userId);

    @Transactional
    void deleteScrappedByUserIdAndPostId(Long userId, Long postId);
}

