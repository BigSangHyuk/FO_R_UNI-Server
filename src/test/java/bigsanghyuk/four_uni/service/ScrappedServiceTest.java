package bigsanghyuk.four_uni.service;

import bigsanghyuk.four_uni.domain.entity.Scrapped;
import bigsanghyuk.four_uni.repository.ScrappedRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        scrappedService.scrap(testUserId, 1L);
        scrappedService.scrap(testUserId, 2L);
        List<Scrapped> after = scrappedService.getScrappedList(testUserId);
        log.info("after scrap:  {}", after.size());
        assertThat(after.size()).isGreaterThan(before.size());
    }

    @DisplayName("스크랩 제거 테스트")
    @Test
    void unScrapTest() {
        List<Scrapped> before = scrappedService.getScrappedList(testUserId);
        log.info("before unScrap: {}", before.size());
        scrappedService.unScrap(testUserId, 100L);
        scrappedService.unScrap(testUserId, 200L);
        List<Scrapped> after = scrappedService.getScrappedList(testUserId);
        log.info("after unScrap:  {}", after.size());
        assertThat(before.size()).isGreaterThan(after.size());
    }

    @BeforeEach
    void beforeEach() {
        log.info("[beforeEach] 테스트 데이터 추가");
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