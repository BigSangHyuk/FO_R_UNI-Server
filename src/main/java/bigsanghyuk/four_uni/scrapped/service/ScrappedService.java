package bigsanghyuk.four_uni.scrapped.service;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.scrapped.domain.entity.Scrapped;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.scrapped.repository.ScrappedRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScrappedService {

    private final ScrappedRepository scrappedRepository;
    private final PostRepository postRepository;

    public List<Scrapped> getScrappedList(@NotNull Long userId) {
        return scrappedRepository.findByUserId(userId);
    }

    public void scrap(@NotNull Long userId, @NotNull Long postId) {
        Optional<Post> post = postRepository.findById(postId);
        Scrapped scrapped = new Scrapped(userId, postId, post.get().getCategoryId());
        //categoryId를 파라미터로 받을 지, Scrapped 생성자에서 categoryId를 뺄지 고민해봐야 될 듯
        scrappedRepository.save(scrapped);
    }

    public void unScrap(@NotNull Long userId, @NotNull Long postId) {
        scrappedRepository.deleteScrappedByUserIdAndPostId(userId, postId);
    }
}
