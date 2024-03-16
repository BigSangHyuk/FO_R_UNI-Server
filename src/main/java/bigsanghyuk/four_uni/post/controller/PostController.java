package bigsanghyuk.four_uni.post.controller;

import bigsanghyuk.four_uni.Results;
import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.dto.request.ScrapRequest;
import bigsanghyuk.four_uni.post.dto.response.GetDetailResponse;
import bigsanghyuk.four_uni.post.service.PostService;
import bigsanghyuk.four_uni.post.service.ScrappedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "posts", description = "게시글 API")
public class PostController {

    private final PostService postService;
    private final ScrappedService scrappedService;

    @Operation(summary = "게시글 단건 조회", description = "조회하고자 하는 게시글 아이디를 파라미터로 전달")
    @GetMapping("/posts")
    public ResponseEntity<GetDetailResponse> getDetail(@RequestParam(name = "postId") Long postId) {
        return ResponseEntity.ok().body(postService.getDetail(postId));
    }

    @Operation(summary = "미분류 게시글 조회", description = "파라미터 없이 호출")
    @GetMapping("/posts/unclassified")
    public ResponseEntity<Results<List<Post>>> getUnclassified() {
        List<Post> unClassifiedPosts = postService.getUnClassifiedLists();
        return ResponseEntity.ok().body(new Results<>(unClassifiedPosts, unClassifiedPosts.size()));
    }

    @Operation(summary = "게시글 필터로 조회", description = "/posts/filter?id=1-2-3-4 이런 식으로 id1-id2-...로 전달")
    @GetMapping("/posts/filter")
    public ResponseEntity<Results<List<Post>>> getByFiltered(@RequestParam(name = "id") String id) {
        List<Long> categoryIds = postService.hyphenStringToList(id, "-");
        List<Post> filteredPosts = postService.getFilteredPostsByCategoryIds(categoryIds);
        return ResponseEntity.ok().body(new Results<>(filteredPosts, filteredPosts.size()));
    }

    @Operation(summary = "스크랩 추가", description = "요청에 userId, postId 담아서 전송")
    @PostMapping("/posts/scrap")
    public ResponseEntity<CommonResponse> scrap(@RequestBody ScrapRequest request) {
        scrappedService.scrap(request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "스크랩 해제", description = "요청에 userId, postId 담아서 전송")
    @DeleteMapping("/posts/unscrap")
    public ResponseEntity<CommonResponse> unScrap(@RequestBody ScrapRequest request) {
        scrappedService.unScrap(request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "스크랩한 게시글 조회", description = "파라미터로  userId 전달")
    @GetMapping("/posts/scrapped")
    public ResponseEntity<Results<List<Post>>> getScrapped(@RequestParam(name = "userId") Long userId) {
        List<Post> scrappedList = postService.getScrappedList(userId);
        return ResponseEntity.ok().body(new Results<>(scrappedList, scrappedList.size()));
    }

    @Operation(summary = "내가 댓글 남긴 글 조회", description = "파라미터로 userId 전달, 최근 댓글 남긴 글이 먼저 나옴")
    @GetMapping("/posts/commented")
    public ResponseEntity<Results<List<Post>>> getPostsCommented(@RequestParam(name = "userId") Long userId) throws IllegalAccessException {
        List<Post> commentedPostList = postService.getCommented(userId);
        return ResponseEntity.ok().body(new Results<>(commentedPostList, commentedPostList.size()));
    }

    @Operation(summary = "해당하는 연, 월의 게시글 조회", description = "2024-03 형식으로 연 월 전달하면 해당 달 앞, 뒤 1달까지의 글 반환")
    @GetMapping("/posts/date")
    public ResponseEntity<List<Post>> getPostByDate(@RequestParam(name = "target") String date) {
        List<Post> postsByDate = postService.getPostsByDate(date);
        return ResponseEntity.ok().body(postsByDate);
    }

    @Getter
    @Setter
    public static class Result<T> {
        private T data;
        private int count;

        public Result(T data, int count) {
            this.data = data;
            this.count = count;
        }
    }
}
