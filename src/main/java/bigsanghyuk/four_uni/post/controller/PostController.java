package bigsanghyuk.four_uni.post.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.post.domain.entity.Post;
import bigsanghyuk.four_uni.post.dto.response.GetPostResponse;
import bigsanghyuk.four_uni.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글 단 건 조회
    @GetMapping("/v1/posts/{postId}")
    public ResponseEntity<CommonResponse> getDetail(@PathVariable Long postId) {
        Post post = postService.getDetail(postId);

        GetPostResponse getPostResponse = GetPostResponse.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .imageUrl(post.getImageUrl())
                .views(post.getViews())
                .build();

        return new ResponseEntity(getPostResponse, HttpStatus.OK);
    }



}