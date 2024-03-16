package bigsanghyuk.four_uni.post.service;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.Scrapped;
import bigsanghyuk.four_uni.post.dto.response.GetDetailResponse;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ScrappedRepository scrappedRepository;
    private final CommentRepository commentRepository;

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

    public List<Post> getCommented(Long userId) throws IllegalAccessException {
        List<Comment> comments = commentRepository.findByUserIdOrderByIdDesc(userId).orElseThrow(IllegalAccessException::new);
        LinkedHashSet<Long> set = new LinkedHashSet<>();
        List<Post> result = new ArrayList<>();
        for (Comment comment : comments) {
            set.add(comment.getPostId());
        }
        for (Long postId : set) {
            result.add(postRepository.findById(postId).orElseThrow(PostNotFoundException::new));
        }
        return result;
    }

    public List<Post> getPostsByDate(String date) { //date format example: 2024-03
        StringTokenizer st = new StringTokenizer(date, "-");
        int targetYear = Integer.parseInt(st.nextToken());
        int targetMonth = Integer.parseInt(st.nextToken());
        LocalDate currentMonth = LocalDate.of(targetYear, targetMonth, 1);
        LocalDate prevMonth = currentMonth.minusMonths(1);
        LocalDate nextMonth = currentMonth.plusMonths(1);

        return postRepository.findPostsByCurrentAndAdjacentMonths(
                currentMonth.getYear(),
                currentMonth.getMonthValue(),
                prevMonth.getYear(),
                prevMonth.getMonthValue(),
                nextMonth.getYear(),
                nextMonth.getMonthValue()
        );
    }
}
