package bigsanghyuk.four_uni.repository;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.service.PostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class PostRepositoryTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void beforeEach() {
        postRepository.save(new Post(1L, false, "hello", "hello kim", null, 0, 0, true, LocalDate.parse("2020-01-08"), LocalDateTime.of(2020, 1, 8, 00, 00, 00)));
        postRepository.save(new Post(2L, false, "ollasdf", "hello kim", null, 0, 0, true, LocalDate.parse("2020-01-08"), LocalDateTime.of(2025, 1, 8, 00, 00, 00)));
        postRepository.save(new Post(3L, false, "jfajg", "egrkjk han", null, 0, 0, true, LocalDate.parse("2023-01-08"), LocalDateTime.of(2024, 1, 8, 00, 00, 00)));
        postRepository.save(new Post(4L, false, "askdfkas", "afdsdsaf jo", null, 0, 0, false, LocalDate.parse("2024-01-08"), LocalDateTime.of(2023, 1, 8, 00, 00, 00)));
    }

    @AfterEach
    void afterEach() {
        postRepository.deleteAll();
    }

    @Test
    void test() {
        List<Post> results = postRepository.findAll();
        assertThat(results.size()).isEqualTo(4);
    }

    @Test
    void findByCategoryIdTest() {
        List<Long> list = new ArrayList<>();
        list.add(1L);
        list.add(2L);
        List<Post> findResult = postRepository.findByCategoryIdIn(list);
        for (Post post : findResult) {
            System.out.println("post=" + post.getTitle() + ", " + post.getContent());
        }
    }

    @Test
    void findByTitleOrContent() {
        String keyword = "kim";
        List<Post> findResult = postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        assertThat(findResult.size()).isEqualTo(2);
        for (Post post : findResult) {
            System.out.println("post=" + post.getTitle() + ", " + post.getContent());
        }
    }

    @Test
    void findByPostedDate() {
        LocalDate start = LocalDate.of(2017, 1, 1);
        LocalDate end = LocalDate.of(2022, 12, 31);
        List<Post> findResult = postRepository.findByPostedAtBetween(start, end);
        for (Post post : findResult) {
            System.out.println("post=" + post.getTitle() + ", " + post.getContent() + ", " + post.getPostedAt());
        }
    }

    @Test
    void findByDeadline() {
        LocalDateTime end = LocalDateTime.of(2023, 12, 31, 23, 59, 00);
        List<Post> findResult = postRepository.findByDeadlineBefore(end);
        for (Post post : findResult) {
            System.out.println("post=" + post.getTitle() + ", " + post.getContent() + ", " + post.getDeadline());
        }
    }
}