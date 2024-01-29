package bigsanghyuk.four_uni.post.service;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public List<Post> searchArticles(List<Long> categories, String keyword,
                                     LocalDate startTime, LocalDate endTime,
                                     LocalDateTime deadline) {
        return null;
    }

    public List<Post> getUnClassifiedLists() {
        return postRepository.findByIsClassifiedFalse();
    }
}