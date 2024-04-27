package bigsanghyuk.four_uni.post.controller;

import bigsanghyuk.four_uni.comment.dto.CommentDto;
import bigsanghyuk.four_uni.common.CommonResponse;
import bigsanghyuk.four_uni.common.Results;
import bigsanghyuk.four_uni.comment.service.CommentService;
import bigsanghyuk.four_uni.post.dto.response.GetDetailResponse;
import bigsanghyuk.four_uni.post.dto.response.GetRequiredResponse;
import bigsanghyuk.four_uni.post.service.PostService;
import bigsanghyuk.four_uni.post.service.ScrappedService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Hidden;
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

    @Hidden
    @Operation(summary = "게시글 등록", description = "김동후 전용")
    @PostMapping("/add-post")
    public ResponseEntity<?> registerPost(@RequestBody String data) throws JsonProcessingException {
        int success = postService.getAddPostResult(data);
        return ResponseEntity.ok().body("{\"articles added\": " + success + "}");
    }

    @Operation(summary = "게시글과 댓글 함께 조회", description = "조회하고자 하는 게시글 아이디를 경로변수로 전달, 댓글 깊이 1 고정")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<Detail> getPostWithComments(@PathVariable(name = "postId") Long postId) {
        GetDetailResponse detail = postService.getDetail(postId);
        List<CommentDto> comments = commentService.getAllComments(postId);
        return ResponseEntity.ok().body(new Detail(detail, comments));
    }

    @Operation(summary = "미분류 게시글 조회", description = "최소 항목")
    @GetMapping("/posts/unclassified")
    public ResponseEntity<Results<List<GetRequiredResponse>>> getUnclassifiedRequiredData() {
        List<GetRequiredResponse> result = postService.getUnclassifiedRequired();
        return ResponseEntity.ok().body(new Results<>(result, result.size()));
    }

    @Operation(summary = "스크랩 추가", description = "요청에 postId 담아서 전송")
    @PostMapping("/posts/scrap/{postId}")
    public ResponseEntity<CommonResponse> scrap(@RequestAttribute(name = "userId") Long userId, @PathVariable(name = "postId") Long postId) {
        scrappedService.scrap(userId, postId);
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "스크랩 해제", description = "요청에 postId 담아서 전송")
    @DeleteMapping("/posts/unscrap/{postId}")
    public ResponseEntity<CommonResponse> unScrap(@RequestAttribute(name = "userId") Long userId, @PathVariable(name = "postId") Long postId) {
        scrappedService.unScrap(userId, postId);
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "스크랩한 게시글 조회", description = "최신순 정렬, 최소 항목")
    @GetMapping("/posts/scrapped")
    public ResponseEntity<Results<List<GetRequiredResponse>>> getScrappedRequiredData(@RequestAttribute(name = "userId") Long userId) {
        List<GetRequiredResponse> scrappedList = postService.getScrappedRequired(userId);
        return ResponseEntity.ok().body(new Results<>(scrappedList, scrappedList.size()));
    }

    @Operation(summary = "내가 댓글 남긴 글 조회", description = "최신순 정렬, 최소 항목")
    @GetMapping("/posts/commented")
    public ResponseEntity<Results<List<GetRequiredResponse>>> getPostCommented(@RequestAttribute(name = "userId") Long userId) {
        List<GetRequiredResponse> commentedPosts = postService.getCommentedPostRequired(userId);
        return ResponseEntity.ok().body(new Results<>(commentedPosts, commentedPosts.size()));
    }

    @Operation(summary = "필터로 해당 월 게시글 조회", description = "2024-04 형식으로 조회하려는 기간 전달, 필터는 /filter?id=1-2-3-4 으로 대시로 구분, 유저의 과 포함하지 않을 시 dept=false로 전달")
    @GetMapping("/posts/filter")
    public ResponseEntity<Results<List<GetRequiredResponse>>> getByFilterAndMonth(@RequestAttribute(name = "userId") Long userIdInToken, @RequestParam(name = "date") String date, @RequestParam(name = "id", required = false) String id, @RequestParam(name = "dept", defaultValue = "true") boolean dept) {
        Long userId = dept ? userIdInToken : null;
        List<GetRequiredResponse> postsRequired = postService.getFilteredRequiredByMonth(date, id, userId);
        return ResponseEntity.ok().body(new Results<>(postsRequired, postsRequired.size()));
    }

    @Operation(summary = "키워드로 검색", description = "검색어 전달, 게시글 등록순 반환, 본문 내용은 100자 까지만")
    @GetMapping("/posts/search")
    public ResponseEntity<Results<List<GetRequiredResponse>>> getByKeyword(@RequestParam(name = "keyword", required = true) String keyword) {
        List<GetRequiredResponse> postsRequired = postService.getByKeyword(keyword);
        return ResponseEntity.ok().body(new Results<>(postsRequired, postsRequired.size()));
    }

    @Getter
    private static class Detail<T> {

        private T detail;
        private List<T> comments;

        private Detail(T detail, List<T> comments) {
            this.detail = detail;
            this.comments = comments;
        }
    }
}
