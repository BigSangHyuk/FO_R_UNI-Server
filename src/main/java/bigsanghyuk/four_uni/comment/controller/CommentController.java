package bigsanghyuk.four_uni.comment.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.dto.request.LikeCommentRequest;
import bigsanghyuk.four_uni.comment.dto.request.RegisterCommentRequest;
import bigsanghyuk.four_uni.comment.service.CommentService;
import bigsanghyuk.four_uni.comment.service.LikeCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "comments", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;
    private final LikeCommentService likeCommentService;

    @Operation(summary = "댓글 등록", description = "URL 경로에 postId 전달")
    @PostMapping("/posts/{postId}/comment")
    public ResponseEntity<CommonResponse> register(@Valid @RequestBody RegisterCommentRequest request) {
        commentService.register(request.toDomain());
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @Operation(summary = "댓글 수정", description = "URL 경로에 postId와 commentId 전달")
    @PutMapping("/posts/{postId}/{commentId}")
    public ResponseEntity<CommonResponse> edit(@PathVariable Long commentId, @Valid @RequestBody EditCommentInfo request) {
        commentService.edit(commentId, request);
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @Operation(summary = "댓글 삭제", description = "URL 경로에 postId와 commentId 전달")
    @DeleteMapping("/posts/{postId}/{commentId}")
    public ResponseEntity<CommonResponse> remove(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.remove(postId, commentId);
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @Operation(summary = "댓글 좋아요", description = "Body 에 값 넣어서 전달")
    @PostMapping("/comments/like")
    public ResponseEntity<CommonResponse> likedComment(@Valid @RequestBody LikeCommentRequest request) {
        likeCommentService.likeComment(request.toDomain());
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

}
