package bigsanghyuk.four_uni;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
public class ExampleData {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PasswordEncoder encoder;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("example data init");
        userRepository.save(new User("example1@example.com", encoder.encode("example1"), "example1", 1, "example1", "example1"));
        userRepository.save(new User("example2@example.com", encoder.encode("example2"), "example2", 2, "example2", "example2"));
        postRepository.save(new Post(1L, false, "example1", "example1", "example1", 1, 0, true, LocalDate.of(2024, 1, 1), LocalDateTime.of(2024, 1, 1, 1, 1, 1)));
        postRepository.save(new Post(2L, false, "example2", "example2", "example2", 2, 0, true, LocalDate.of(2024, 2, 2), LocalDateTime.of(2024, 2, 2, 2, 2, 2)));
        postRepository.save(new Post(3L, false, "example3", "example3", "example3", 3, 0, false, LocalDate.of(2024, 3, 3), LocalDateTime.of(2024, 3, 3, 3, 3, 3)));
        postRepository.save(new Post(4L, false, "example4", "example4", "example4", 4, 0, false, LocalDate.of(2024, 4, 4), LocalDateTime.of(2024, 4, 4, 4, 4, 4)));
    }
}
