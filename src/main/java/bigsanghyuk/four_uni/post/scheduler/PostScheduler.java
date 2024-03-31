package bigsanghyuk.four_uni.post.scheduler;

import bigsanghyuk.four_uni.post.service.PostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostScheduler {

    private final WebClient webClient;
    private final PostService postService;

    @Scheduled(cron = "0 5 9-18 * * 1-5") // 평일 09~18시 05분마다 실행
    public void getPostData() {
        Mono<String> responseMono = webClient.get()
                .uri("/")   // 목적지 서버의 controller 매핑 (추가해야됨)
                .retrieve()
                .bodyToMono(String.class);

        responseMono.subscribe(this::addPost);  // 비동기 처리
    }

    private void addPost(String response) {
        String data = "\"" + response + "\"";
        try {
            int success = postService.getAddPostResult(data);
            log.info("articles added={}, time={}", success, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
