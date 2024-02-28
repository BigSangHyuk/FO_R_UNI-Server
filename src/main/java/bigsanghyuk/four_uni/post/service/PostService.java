package bigsanghyuk.four_uni.post.service;

import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.Scrapped;
import bigsanghyuk.four_uni.post.dto.response.GetDetailResponse;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ScrappedRepository scrappedRepository;

    public List<Post> getUnClassifiedLists() {
        return postRepository.findByIsClassifiedFalse();
    }

    public GetDetailResponse getDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        GetDetailResponse response = GetDetailResponse.builder()
                .id(postId)
                .categoryId(post.getCategoryId())
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .views(post.getViews())
                .isClassified(post.isClassified())
                .postedAt(post.getPostedAt())
                .deadline(post.getDeadline())
                .build();
        return response;
    }

    public List<Post> getFilteredPostsByCategoryIds(List<Long> categoryIds) {
        return postRepository.findByCategoryIdIn(categoryIds);
    }

    public List<Long> hyphenStringToList(String input, String delimiter) {
        List<Long> result = new ArrayList<>();
        String[] tokens = input.split(delimiter);
        for (String token : tokens) {
            result.add(Long.parseLong(token));
        }
        return result;
    }

    public List<Post> getScrappedList(Long userId) {
        Iterator<Scrapped> it = scrappedRepository.findByUserIdOrderByScrappedAt(userId).iterator();
        LinkedList<Post> scrappedList = new LinkedList<>();
        while (it.hasNext()) {
            Long postId = it.next().getPostId();
            Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
            scrappedList.add(post);
        }
        return scrappedList;
    }
}
