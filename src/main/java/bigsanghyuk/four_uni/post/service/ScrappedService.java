package bigsanghyuk.four_uni.post.service;

import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.post.domain.ScrapInfo;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.Scrapped;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScrappedService {

    private final ScrappedRepository scrappedRepository;
    private final PostRepository postRepository;

    public List<Scrapped> getScrappedList(@NotNull Long userId) {
        return scrappedRepository.findByUserIdOrderByScrappedAt(userId);
    }

    public void scrap(ScrapInfo scrapInfo) {
        Post post = postRepository.findById(scrapInfo.getPostId())
                .orElseThrow(PostNotFoundException::new);

        Scrapped scrapped = new Scrapped(scrapInfo.getUserId(), scrapInfo.getPostId(), post.getCategoryId());
        scrappedRepository.save(scrapped);
    }

    public void unScrap(ScrapInfo scrapInfo) {
        scrappedRepository.deleteScrappedByUserIdAndPostId(scrapInfo.getUserId(), scrapInfo.getPostId());
    }
}
