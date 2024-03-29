package bigsanghyuk.four_uni.post.service;

import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.post.domain.RegisterPostInfo;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.Scrapped;
import bigsanghyuk.four_uni.post.dto.request.RegisterPostRequest;
import bigsanghyuk.four_uni.post.dto.response.GetDetailResponse;
import bigsanghyuk.four_uni.post.domain.entity.PostRequired;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ScrappedRepository scrappedRepository;
    private final CommentRepository commentRepository;

    public List<RegisterPostInfo> jsonToDto(String data) throws JsonProcessingException {
        JSONParser jsonParser = new JSONParser();
        JSONArray array = new JSONArray();
        ObjectMapper objectMapper = new ObjectMapper();
        List<RegisterPostInfo> infos = new ArrayList<>();
        try {
            array = (JSONArray) jsonParser.parse(data);
        } catch (ParseException e) {
            log.error("error={}", e.toString());
        }
        for (Object obj : array) {
            infos.add(objectMapper.readValue(((JSONObject) obj).toJSONString(), RegisterPostRequest.class).toDomain());
        }
        return infos;
    }

    public int addPost(RegisterPostInfo registerPostInfo) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        LocalDate postedAt = LocalDate.parse(registerPostInfo.getPostedAt(), formatter);
        LocalDate deadline;
        try {
            deadline = LocalDate.parse(registerPostInfo.getDeadline(), formatter);
        } catch (DateTimeParseException e) {
            deadline = null;
        }
        if (postRepository.existsPostByNoticeUrl(registerPostInfo.getNoticeUrl())) {
            return 0;
        } else {
            postRepository.save(
                    Post.builder()
                            .categoryId(registerPostInfo.getCategoryId())
                            .title(registerPostInfo.getTitle())
                            .content(registerPostInfo.getContent())
                            .imageUrl(registerPostInfo.getImageUrl())
                            .isClassified(registerPostInfo.getIsClassified())
                            .postedAt(postedAt)
                            .deadline(deadline)
                            .noticeUrl(registerPostInfo.getNoticeUrl())
                            .build());
            return 1;
        }
    }

    public List<Post> getUnClassifiedLists() {
        return postRepository.findByIsClassifiedFalse();
    }

    public List<PostRequired> getUnclassifiedRequired() {
        return postRepository.findRequiredIsClassifiedFalse();
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
                .noticeUrl(post.getNoticeUrl())
                .reported(post.isReported())
                .postReportCount(post.getPostReportCount())
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
        DateFilter filter = new DateFilter(st);

        return postRepository.findPostsByCurrentAndAdjacentMonths(
                filter.getCurrentMonth().getYear(),
                filter.getCurrentMonth().getMonthValue(),
                filter.getPrevMonth().getYear(),
                filter.getPrevMonth().getMonthValue(),
                filter.getNextMonth().getYear(),
                filter.getNextMonth().getMonthValue()
        );
    }

    public List<PostRequired> getPostsByDateRequired(String date) { //date format example: 2024-03
        StringTokenizer st = new StringTokenizer(date, "-");
        DateFilter filter = new DateFilter(st);

        return postRepository.findRequiredByCurrentAndAdjacentMonths(
                filter.getCurrentMonth().getYear(),
                filter.getCurrentMonth().getMonthValue(),
                filter.getPrevMonth().getYear(),
                filter.getPrevMonth().getMonthValue(),
                filter.getNextMonth().getYear(),
                filter.getNextMonth().getMonthValue()
        );
    }

    @Getter
    static class DateFilter {

        private int targetYear;
        private int targetMonth;
        private LocalDate currentMonth;
        private LocalDate prevMonth;
        private LocalDate nextMonth;

        public DateFilter(StringTokenizer st) {
            this.targetYear = Integer.parseInt(st.nextToken());
            this.targetMonth = Integer.parseInt(st.nextToken());
            this.currentMonth = LocalDate.of(targetYear, targetMonth, 1);
            this.prevMonth = currentMonth.minusMonths(1);
            this.nextMonth = currentMonth.plusMonths(1);
        }
    }
}
