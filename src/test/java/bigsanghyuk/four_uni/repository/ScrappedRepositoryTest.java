package bigsanghyuk.four_uni.repository;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.domain.entity.Scrapped;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Slf4j
class ScrappedRepositoryTest {

    @Autowired
    PostRepository postRepository;
    @Autowired
    ScrappedRepository scrappedRepository;

    static final long userId1 = 1L;
    static final long userId2 = 2L;
    static final long userId3 = 3L;

    /*@DisplayName("스크랩된 게시물의 개수을 찾는 테스트")
    @Test
    void findScrappedListSizeTest() {
        List<Scrapped> scrappedListUser1 = scrappedRepository.findByUserId(userId1);
        List<Scrapped> scrappedListUser2 = scrappedRepository.findByUserId(userId2);

        assertThat(scrappedListUser1.size()).isEqualTo(4);
        assertThat(scrappedListUser2.size()).isEqualTo(6);
    }*/

    @DisplayName("스크랩된 게시물을 찾는 테스트")
    @Test
    void printScrappedListTest() {
        List<Scrapped> scrappedListUser = scrappedRepository.findByUserId(userId2);
        for (int i = 0; i < scrappedListUser.size(); i++) {
            Scrapped scrapped = scrappedListUser.get(i);
            Optional<Post> post = postRepository.findById(scrapped.getPostId());
            log.info("Scrapped{} -> title={}, scrapId={}, scrapPostId={}", i + 1, post.get().getTitle(), scrapped.getId(), scrapped.getPostId());
            //출력: scrapped1 | title=hello, scrap Id=71, scrap PostId=239
        }
    }

    @DisplayName("스크랩 제거 테스트")
    @Test
    void removeScrapTest() {
        log.info("before deletion={}", scrappedRepository.findByUserId(userId2).size());
        log.info("delete column twice");
        for (int i = 0; i < 2; i++) {
            scrappedRepository.deleteScrappedByUserIdAndPostId(userId2, scrappedRepository.findByUserId(userId2).get(0).getPostId());   //postId 파라미터는 userId2의 제일 첫 글 -> 첫 글 삭제후 결과가 텍스트 결과
        }
        log.info("after deletion={}", scrappedRepository.findByUserId(userId2).size());
    }

    @DisplayName("스크랩된 게시물이 없는 테스트")
    @Test
    void noScrappedTest() {
        List<Scrapped> scrappedList = scrappedRepository.findByUserId(userId3);
        assertThat(scrappedList.size()).isEqualTo(0);
    }

    @BeforeEach
    void beforeEach() {
        log.info("[beforeEach] 테스트 데이터 추가");
        Post save1 = postRepository.save(new Post(1L, false, "hello", "hello kim", null, 0, 0, true, LocalDate.parse("2020-01-08"), LocalDateTime.of(2020, 1, 8, 00, 00, 00)));
        Post save2 = postRepository.save(new Post(2L, false, "ollasdf", "hello kim", null, 0, 0, true, LocalDate.parse("2020-01-08"), LocalDateTime.of(2025, 1, 8, 00, 00, 00)));
        Post save3 = postRepository.save(new Post(3L, false, "jfajg", "egrkjk han", null, 0, 0, true, LocalDate.parse("2023-01-08"), LocalDateTime.of(2024, 1, 8, 00, 00, 00)));
        Post save4 = postRepository.save(new Post(4L, false, "askdfkas", "afdsdsaf jo", null, 0, 0, false, LocalDate.parse("2024-01-08"), LocalDateTime.of(2023, 1, 8, 00, 00, 00)));
        Post save5 = postRepository.save(new Post(1L, false, "asdfsadfff", "hello kim", null, 0, 0, true, LocalDate.parse("2020-01-08"), LocalDateTime.of(2020, 1, 8, 00, 00, 00)));
        Post save6 = postRepository.save(new Post(2L, false, "ollasdf", "hello kim", null, 0, 0, true, LocalDate.parse("2020-01-08"), LocalDateTime.of(2025, 1, 8, 00, 00, 00)));
        Post save7 = postRepository.save(new Post(3L, false, "jfajg", "egrkjk han", null, 0, 0, true, LocalDate.parse("2023-01-08"), LocalDateTime.of(2024, 1, 8, 00, 00, 00)));
        Post save8 = postRepository.save(new Post(2L, false, "ollasdf", "hello kim", null, 0, 0, true, LocalDate.parse("2020-01-08"), LocalDateTime.of(2025, 1, 8, 00, 00, 00)));
        Post save9 = postRepository.save(new Post(3L, false, "jfajg", "egrkjk han", null, 0, 0, true, LocalDate.parse("2023-01-08"), LocalDateTime.of(2024, 1, 8, 00, 00, 00)));
        Post save10 = postRepository.save(new Post(4L, false, "askdfkas", "afdsdsaf jo", null, 0, 0, false, LocalDate.parse("2024-01-08"), LocalDateTime.of(2023, 1, 8, 00, 00, 00)));

        scrappedRepository.save(new Scrapped(userId1, save1.getId(), save1.getCategoryId()));   //hello
        scrappedRepository.save(new Scrapped(userId1, save2.getId(), save2.getCategoryId()));   //ollasdf
        scrappedRepository.save(new Scrapped(userId1, save3.getId(), save3.getCategoryId()));   //jfajg
        scrappedRepository.save(new Scrapped(userId1, save8.getId(), save8.getCategoryId()));   //ollasdf

        scrappedRepository.save(new Scrapped(userId2, save4.getId(), save4.getCategoryId()));
        scrappedRepository.save(new Scrapped(userId2, save5.getId(), save5.getCategoryId()));
        scrappedRepository.save(new Scrapped(userId2, save6.getId(), save6.getCategoryId()));
        scrappedRepository.save(new Scrapped(userId2, save7.getId(), save7.getCategoryId()));
        scrappedRepository.save(new Scrapped(userId2, save9.getId(), save9.getCategoryId()));
        scrappedRepository.save(new Scrapped(userId2, save10.getId(), save10.getCategoryId()));
    }

    @AfterEach
    void afterEach() {
        log.info("[afterEach]  테스트 데이터 삭제");
        postRepository.deleteAll();
        scrappedRepository.deleteAll();
    }
}