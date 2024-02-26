package bigsanghyuk.four_uni.service;

import bigsanghyuk.four_uni.post.domain.ScrapInfo;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.Scrapped;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
import bigsanghyuk.four_uni.post.service.ScrappedService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class ScrappedServiceTest {

    static final long testUserId = 1L;

    @Autowired
    ScrappedService scrappedService;
    @Autowired
    ScrappedRepository scrappedRepository;
    @Autowired
    PostRepository postRepository;

    @DisplayName("스크랩된 게시물 반환받기 테스트")
    @Test
    void scrappedListTest() {
        List<Scrapped> scrappedList = scrappedService.getScrappedList(testUserId);
        assertThat(scrappedList.size()).isEqualTo(5);
    }

    @DisplayName("스크랩 실행 테스트")
    @Test
    void scrapTest() {
        List<Scrapped> before = scrappedService.getScrappedList(testUserId);
        log.info("before scrap: {}", before.size());
        ScrapInfo scrapInfo1 = new ScrapInfo(testUserId, 1L);
        ScrapInfo scrapInfo2 = new ScrapInfo(testUserId, 2L);
        scrappedService.scrap(scrapInfo1);
        scrappedService.scrap(scrapInfo2);
        List<Scrapped> after = scrappedService.getScrappedList(testUserId);
        log.info("after scrap:  {}", after.size());
        assertThat(after.size()).isGreaterThan(before.size());
    }

    @DisplayName("스크랩 제거 테스트")
    @Test
    void unScrapTest() {
        List<Scrapped> before = scrappedService.getScrappedList(testUserId);
        log.info("before unScrap: {}", before.size());
        ScrapInfo scrapInfo1 = new ScrapInfo(testUserId, 100L);
        ScrapInfo scrapInfo2 = new ScrapInfo(testUserId, 200L);
        scrappedService.unScrap(scrapInfo1);
        scrappedService.unScrap(scrapInfo2);
        List<Scrapped> after = scrappedService.getScrappedList(testUserId);
        log.info("after unScrap:  {}", after.size());
        assertThat(before.size()).isGreaterThan(after.size());
    }

    @BeforeEach
    void beforeEach() {
        log.info("[beforeEach] 테스트 데이터 추가");
        postRepository.save(new Post(1L, false, "test1", "test1", "test1", 0, 0, true, LocalDate.now(), LocalDateTime.now()));
        postRepository.save(new Post(2L, false, "test2", "test2", "test2", 0, 0, true, LocalDate.now(), LocalDateTime.now()));
        scrappedRepository.save(new Scrapped(testUserId, 100L, 10000L));
        scrappedRepository.save(new Scrapped(testUserId, 200L, 20000L));
        scrappedRepository.save(new Scrapped(testUserId, 300L, 30000L));
        scrappedRepository.save(new Scrapped(testUserId, 400L, 40000L));
        scrappedRepository.save(new Scrapped(testUserId, 500L, 50000L));
    }

    @AfterEach
    void afterEach() {
        log.info("[afterEach]  테스트 데이터 삭제");
        scrappedRepository.deleteAll();
    }
}