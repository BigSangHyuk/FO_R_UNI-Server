package bigsanghyuk.four_uni.post.service;

import bigsanghyuk.four_uni.comment.domain.entity.CommentProfile;
import bigsanghyuk.four_uni.comment.repository.CommentRepository;
import bigsanghyuk.four_uni.exception.post.PostNotFoundException;
import bigsanghyuk.four_uni.exception.post.WrongDateFormatException;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.post.domain.RegisterPostInfo;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.ScrappedRequired;
import bigsanghyuk.four_uni.post.dto.request.RegisterPostRequest;
import bigsanghyuk.four_uni.post.dto.response.GetDetailResponse;
import bigsanghyuk.four_uni.post.domain.entity.PostRequired;
import bigsanghyuk.four_uni.post.dto.response.GetRequiredResponse;
import bigsanghyuk.four_uni.post.repository.PostRepository;
import bigsanghyuk.four_uni.post.repository.ScrappedRepository;
import bigsanghyuk.four_uni.user.domain.entity.User;
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
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final ScrappedRepository scrappedRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private static final String DATE_FORMAT_REGEX = "\\d{4}-\\d{2}";

    public int getAddPostResult(String data) throws JsonProcessingException {
        int success = 0;
        List<RegisterPostInfo> infos = jsonToDto(data);
        CategoryType[] values = CategoryType.values();
        for (RegisterPostInfo info : infos) {
            for (CategoryType value : values) {
                if (value.getId() == (int) (long) info.getCategoryId()) {
                    success += addPost(info, CategoryType.valueOf(value.getKey()));
                    break;
                }
            }
        }
        return success;
    }

    public List<GetRequiredResponse> getUnclassifiedRequired() {
        List<PostRequired> required = postRepository.findRequiredIsClassifiedFalse();
        return convertToDto(required);
    }

    public GetDetailResponse getDetail(Long userId, Long postId) {
        User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Post post = postRepository.findById(postId).orElseThrow(PostNotFoundException::new);
        boolean isScrapped = scrappedRepository.existsByUserAndPost(user, post);
        return new GetDetailResponse(post, isScrapped);
    }

    public List<GetRequiredResponse> getFilteredRequiredByMonth(String date, String ids) {
        List<Long> categoryIds = stringToCategoryIds(ids);
        List<String> categoryNames = getCategoryNames(categoryIds);
        Map<String, Integer> map = makeDateMap(date);
        List<PostRequired> required = postRepository.findFiltered(categoryNames, map.get("year"), map.get("month"));
        return convertToDto(required);
    }

    public List<GetRequiredResponse> getScrappedRequired(Long userId) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        List<ScrappedRequired> required = scrappedRepository.findRequired(userId);
        List<PostRequired> result = getScrappedInfo(required);
        return convertToDto(result);
    }

    public List<GetRequiredResponse> getCommentedPostRequired(Long userId) {
        List<CommentProfile> commentedPostId = commentRepository.findCommentedPostId(userId);
        List<PostRequired> required = getCommented(commentedPostId);
        return convertToDto(required);
    }

    public List<GetRequiredResponse> getByKeyword(String keyword) {
        List<PostRequired> required = postRepository.findRequiredByKeyword(keyword);
        return convertToDto(required);
    }

    private List<PostRequired> getCommented(List<CommentProfile> commentedPostId) {
        LinkedList<PostRequired> result = new LinkedList<>();
        for (CommentProfile commentRequired : commentedPostId) {
            Long postId = commentRequired.getPostId();
            result.add(postRepository.findRequiredByPostId(postId));
        }
        return result;
    }

    private List<String> getCategoryNames(List<Long> categoryIds) {
        List<String> categoryNames = new ArrayList<>();
        CategoryType[] values = CategoryType.values();
        for (Long id : categoryIds) {
            for (CategoryType value : values) {
                if ((int) value.getId() == id) {
                    categoryNames.add(value.getKey());
                    break;
                }
            }
        }
        return categoryNames;
    }

    private List<Long> stringToCategoryIds(String input) {
        List<Long> result = new ArrayList<>();
        String[] tokens = input.split("-");
        for (String token : tokens) {
            if (token.isEmpty()) {
                continue;
            }
            result.add(Long.parseLong(token));
        }
        return result;
    }

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

    private int addPost(RegisterPostInfo registerPostInfo, CategoryType categoryType) {
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

    private List<PostRequired> getScrappedInfo(List<ScrappedRequired> required) {
        LinkedList<PostRequired> infos = new LinkedList<>();
        for (ScrappedRequired scrappedRequired : required) {
            PostRequired found = postRepository.findRequiredByPostId(scrappedRequired.getPostId());
            infos.add(found);
        }
        return infos;
    }

    private Map<String, Integer> makeDateMap(String date) {
        HashMap<String, Integer> map = new HashMap<>();

        if (!Pattern.matches(DATE_FORMAT_REGEX, date)) {
            throw new WrongDateFormatException();
        }

        String[] yearAndMonth = date.split("-");
        map.put("year", Integer.parseInt(yearAndMonth[0]));
        map.put("month", Integer.parseInt(yearAndMonth[1]));
        return map;
    }

    private List<GetRequiredResponse> convertToDto(List<PostRequired> postRequired) {
        List<GetRequiredResponse> dtos = new ArrayList<>();
        for (PostRequired post : postRequired) {
            dtos.add(getRequiredResponseBuilder(post));
        }
        return dtos;
    }

    private GetRequiredResponse getRequiredResponseBuilder(PostRequired postRequired) {
        return GetRequiredResponse.builder()
                .postId(postRequired.getPostId())
                .category(postRequired.getCategory().getValue())
                .title(postRequired.getTitle())
                .content(postRequired.getContent())
                .deadline(postRequired.getDeadline())
                .build();
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
