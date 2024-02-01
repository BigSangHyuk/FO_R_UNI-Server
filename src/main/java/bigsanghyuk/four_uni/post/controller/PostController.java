package bigsanghyuk.four_uni.post.controller;

import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.dto.response.GetPostResponse;
import bigsanghyuk.four_uni.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "posts", description = "게시글 API")
public class PostController {

    private final PostService postService;

    // 게시글 단 건 조회
//    @GetMapping("/v1/posts/{postId}")
//    public ResponseEntity<GetPostResponse> getDetail(@PathVariable Long postId) {
//        Post post = postService.getDetail(postId);
//
//        GetPostResponse getPostResponse = GetPostResponse.builder()
//                .title(post.getTitle())
//                .content(post.getContent())
//                .imageUrl(post.getImageUrl())
//                .views(post.getViews())
//                .build();
//
//        return new ResponseEntity(getPostResponse, HttpStatus.OK);
//    }

    @Operation(summary = "게시글 단건 조회", description = "해당 게시글 아이디만 경로 변수로 전달")
    @GetMapping("/v1/posts/{postId}")
    public ResponseEntity<GetPostResponse> getDetail(@PathVariable("postId") Long postId) {
        Post post = postService.getDetail(postId);

        GetPostResponse getPostResponse = GetPostResponse.builder()
                .id(postId)
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .views(post.getViews())
                .build();

        return new ResponseEntity(getPostResponse, HttpStatus.OK);
    }

    @Operation(summary = "미분류 게시글 조회", description = "파라미터 없이 호출")
    @GetMapping("/v1/posts/unclassified")
    public ResponseEntity<Result<List<Post>>> getUnclassified() {
        List<Post> unClassifiedPosts = postService.getUnClassifiedLists();
        return ResponseEntity.ok().body(new Result<>(unClassifiedPosts, unClassifiedPosts.size()));
    }

    @Operation(summary = "게시글 필터로 조회", description = "/v1/posts/filter?id=1-2-3-4 이런 식으로 id1-id2-...로 전달")
    @GetMapping("/v1/posts/filter")
    public ResponseEntity<Result<List<Post>>> getByFiltered(@RequestParam(name = "id") String id) {
        List<Long> categoryIds = postService.hyphenStringToList(id, "-");
        List<Post> filteredPosts = postService.getFilteredPostsByCategoryIds(categoryIds);
        return ResponseEntity.ok().body(new Result<>(filteredPosts, filteredPosts.size()));
    }

    @Getter @Setter
    public static class Result<T> {
        private T data;
        private int count;

        public Result(T data, int count) {
            this.data = data;
            this.count = count;
        }
    }
}
