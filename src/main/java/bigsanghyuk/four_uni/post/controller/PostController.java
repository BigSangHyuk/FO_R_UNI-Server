package bigsanghyuk.four_uni.post.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.Results;
import bigsanghyuk.four_uni.comment.dto.CommentDto;
import bigsanghyuk.four_uni.comment.service.CommentService;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.domain.entity.PostRequired;
import bigsanghyuk.four_uni.post.dto.request.ScrapRequest;
import bigsanghyuk.four_uni.post.dto.response.GetDetailResponse;
import bigsanghyuk.four_uni.post.service.PostService;
import bigsanghyuk.four_uni.post.service.ScrappedService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "posts", description = "게시글 API")
public class PostController {

    private final PostService postService;
    private final ScrappedService scrappedService;
    private final CommentService commentService;

    @Operation(summary = "게시글 등록", description = "김동후 전용")
    @PostMapping("/add-post")
    public ResponseEntity<?> registerPost(@RequestBody String data) throws JsonProcessingException {
        int success = postService.getAddPostResult(data);
        return ResponseEntity.ok().body("{\"articles added\": " + success + "}");
    }

    @Operation(summary = "게시글과 댓글 함께 조회", description = "조회하고자 하는 게시글 아이디를 경로변수로 전달, 댓글 깊이 1 고정")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<Details> getPostWithComments(@PathVariable(name = "postId") Long postId) {
        GetDetailResponse detail = postService.getDetail(postId);
        List<CommentDto> comments = commentService.getAllComments(postId);
        return ResponseEntity.ok().body(new Details(detail, comments));
    }

    @Operation(summary = "미분류 게시글 조회", description = "최소 항목")
    @GetMapping("/posts/unclassified")
    public ResponseEntity<Results<List<PostRequired>>> getUnclassifiedRequiredData() {
        List<PostRequired> result = postService.getUnclassifiedRequired();
        return ResponseEntity.ok().body(new Results<>(result, result.size()));
    }

    @Operation(summary = "게시글 필터 조회", description = "/posts/filter?id=1-2-3-4 이런 식으로 전달, 최소 항목")
    @GetMapping("/posts/filter")
    public ResponseEntity<Results<List<Post>>> getByFilteredRequiredData(@RequestParam(name = "id") String id) {
        List<Long> categoryIds = postService.hyphenStringToList(id, "-");
        List<Post> filteredPosts = postService.getFilteredPostsRequired(categoryIds);
        return ResponseEntity.ok().body(new Results<>(filteredPosts, filteredPosts.size()));
    }

    @Operation(summary = "스크랩 추가", description = "요청에 postId 담아서 전송")
    @PostMapping("/posts/scrap")
    public ResponseEntity<CommonResponse> scrap(@RequestAttribute(name = "userId") Long userId, @RequestBody ScrapRequest request) {
        scrappedService.scrap(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "스크랩 해제", description = "요청에 postId 담아서 전송")
    @DeleteMapping("/posts/unscrap")
    public ResponseEntity<CommonResponse> unScrap(@RequestAttribute(name = "userId") Long userId, @RequestBody ScrapRequest request) {
        scrappedService.unScrap(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "스크랩한 게시글 조회", description = "최신순 정렬, 최소 항목")
    @GetMapping("/posts/scrapped")
    public ResponseEntity<Results<List<PostRequired>>> getScrappedRequiredData(@RequestAttribute(name = "userId") Long userId) {
        List<PostRequired> scrappedList = postService.getScrappedRequired(userId);
        return ResponseEntity.ok().body(new Results<>(scrappedList, scrappedList.size()));
    }

    @Operation(summary = "내가 댓글 남긴 글 조회", description = "최신순 정렬, 최소 항목")
    @GetMapping("/posts/commented")
    public ResponseEntity<Results<List<PostRequired>>> getPostCommented(@RequestAttribute(name = "userId") Long userId) {
        List<PostRequired> commentedPosts = postService.getCommentedPostRequired(userId);
        return ResponseEntity.ok().body(new Results<>(commentedPosts, commentedPosts.size()));
    }

    @Operation(summary = "해당하는 기간의 게시글 조회", description = "2024-03 형식으로 연 월 전달하면 해당 달 앞, 뒤 1달까지의 글 반환, 최소 항목")
    @GetMapping("/posts/date")
    public ResponseEntity<List<PostRequired>> getPostByDateRequiredData(@RequestParam(name = "target") String date) {
        List<PostRequired> postsRequired = postService.getPostsByDateRequired(date);
        return ResponseEntity.ok().body(postsRequired);
    }

    @Getter
    public static class Details {

        private GetDetailResponse detail;
        private List<CommentDto> comments;

        public Details(GetDetailResponse detail, List<CommentDto> comments) {
            this.detail = detail;
            this.comments = comments;
        }
    }
}
