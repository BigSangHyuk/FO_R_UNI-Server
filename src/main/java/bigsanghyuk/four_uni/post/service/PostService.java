package bigsanghyuk.four_uni.post.service;

import bigsanghyuk.four_uni.comment.domain.entity.CommentProfile;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.post.domain.RegisterPostInfo;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.ScrappedRequired;
import bigsanghyuk.four_uni.post.dto.request.RegisterPostRequest;
import bigsanghyuk.four_uni.post.dto.response.GetDetailResponse;
import bigsanghyuk.four_uni.post.domain.entity.PostRequired;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
import bigsanghyuk.four_uni.user.enums.CategoryType;
import bigsanghyuk.four_uni.user.repository.UserRepository;
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
    private final UserRepository userRepository;

    private List<RegisterPostInfo> jsonToDto(String data) throws JsonProcessingException {
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

    public int getAddPostResult(String data) throws JsonProcessingException {
        int success = 0;
        List<RegisterPostInfo> infos = jsonToDto(data);
        CategoryType[] values = CategoryType.values();
        for (RegisterPostInfo info : infos) {
            for (CategoryType value : values) {
                if (value.getId() == (int) (long) info.getCategoryId()) {
                    success += addPost(info, CategoryType.valueOf(value.getKey()));
                }
            }
        }
        return success;
    }

    public int addPost(RegisterPostInfo registerPostInfo, CategoryType categoryType) {
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
                            .categoryType(categoryType)
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

    public List<PostRequired> getUnclassifiedRequired() {
        return postRepository.findRequiredIsClassifiedFalse();
    }

    public GetDetailResponse getDetail(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        GetDetailResponse response = GetDetailResponse.builder()
                .id(postId)
                .categoryType(post.getCategoryType())
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

    public List<PostRequired> getFilteredRequired(String ids) {
        List<Long> categoryIds = stringToCategoryIds(ids);
        List<String> categoryNames = getCategoryNames(categoryIds);

        return postRepository.findPostRequiredFiltered(categoryNames);
    }

    public List<PostRequired> getScrappedRequired(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<ScrappedRequired> required = scrappedRepository.findRequired(userId);
        LinkedList<PostRequired> posts = new LinkedList<>();
        for (ScrappedRequired scrappedRequired : required) {
            PostRequired found = postRepository.findRequiredByPostId(scrappedRequired.getPostId());
            posts.add(found);
        }
        return posts;
    }

    public List<PostRequired> getCommentedPostRequired(Long userId) {
        List<CommentProfile> commentedPostId = commentRepository.findCommentedPostId(userId);
        LinkedHashSet<Long> set = new LinkedHashSet<>();
        LinkedList<PostRequired> result = new LinkedList<>();
        for (CommentProfile commentRequired : commentedPostId) {
            set.add(commentRequired.getPostId());
        }
        for (Long postId : set) {
            result.add(postRepository.findRequiredByPostId(postId));
        }
        return result;
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

    private List<String> getCategoryNames(List<Long> categoryIds) {
        List<String> categoryNames = new ArrayList<>();
        CategoryType[] values = CategoryType.values();
        for (Long id : categoryIds) {
            for (CategoryType value : values) {
                if ((int) value.getId() == id) {
                    categoryNames.add(value.getKey());
                }
            }
        }
        return categoryNames;
    }

    private List<Long> stringToCategoryIds(String input) {
        List<Long> result = new ArrayList<>();
        String[] tokens = input.split("-");
        for (String token : tokens) {
            result.add(Long.parseLong(token));
        }
        return result;
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
