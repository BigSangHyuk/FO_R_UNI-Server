package bigsanghyuk.four_uni.post.service;

import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.exception.scrapped.AlreadyScrappedException;
import bigsanghyuk.four_uni.exception.scrapped.ScrapNotFoundException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.post.domain.ScrapInfo;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.Scrapped;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrappedService {

    private final ScrappedRepository scrappedRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void scrap(Long userId, ScrapInfo scrapInfo) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(scrapInfo.getPostId())
                .orElseThrow(PostNotFoundException::new);
        scrappedRepository.findByUserAndPost(user, post)
                .ifPresent(scrapped -> {
                            throw new AlreadyScrappedException();
                        }
                );
        scrappedRepository.save(new Scrapped(user, post));
    }

    public void unScrap(Long userId, ScrapInfo scrapInfo) {
        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(scrapInfo.getPostId())
                .orElseThrow(PostNotFoundException::new);
        scrappedRepository.findByUserAndPost(user, post)
                .orElseThrow(ScrapNotFoundException::new);
        scrappedRepository.deleteScrappedByUserAndPost(user, post);
    }
}
