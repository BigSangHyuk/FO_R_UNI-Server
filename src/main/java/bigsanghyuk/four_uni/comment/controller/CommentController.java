package bigsanghyuk.four_uni.comment.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.Results;
import bigsanghyuk.four_uni.comment.domain.entity.LikeComment;
import bigsanghyuk.four_uni.comment.dto.request.*;
import bigsanghyuk.four_uni.comment.domain.entity.Comment;
import bigsanghyuk.four_uni.comment.dto.request.EditCommentRequest;
import bigsanghyuk.four_uni.comment.dto.request.LikeCommentRequest;
import bigsanghyuk.four_uni.comment.dto.request.RegisterCommentRequest;
import bigsanghyuk.four_uni.comment.dto.request.UnLikeCommentRequest;
import bigsanghyuk.four_uni.comment.service.CommentService;
import bigsanghyuk.four_uni.comment.service.LikeCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "comments", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;
    private final LikeCommentService likeCommentService;

    @Operation(summary = "댓글 등록")
    @PostMapping("/comments")
    public ResponseEntity<CommonResponse> register(@Valid @RequestBody RegisterCommentRequest request) {
        commentService.register(request.toDomain());
        return new ResponseEntity<>(new CommonResponse(true), HttpStatus.OK);
    }

    @Operation(summary = "댓글 수정", description = "URL 경로에 commentId 전달")
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommonResponse> edit(@PathVariable(name = "commentId") Long commentId, @Valid @RequestBody EditCommentRequest request) {
        commentService.edit(commentId, request.toDomain());
        return new ResponseEntity<>(new CommonResponse(true), HttpStatus.OK);
    }

    @Operation(summary = "댓글 삭제", description = "URL 경로에 commentId 전달")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<CommonResponse> remove(@PathVariable("commentId") Long commentId, @Valid @RequestBody DeleteCommentRequest request) {
        commentService.remove(commentId, request.toDomain());
        return new ResponseEntity<>(new CommonResponse(true), HttpStatus.OK);
    }

    @Operation(summary = "댓글 좋아요", description = "Body 에 값 넣어서 전달")
    @PostMapping("/comments/like")
    public ResponseEntity<CommonResponse> likeComment(@Valid @RequestBody LikeCommentRequest request) {
        likeCommentService.likeComment(request.toDomain());
        return new ResponseEntity<>(new CommonResponse(true), HttpStatus.OK);
    }

    @Operation(summary = "댓글 좋아요 취소", description = "Body 에 값 넣어서 전달")
    @DeleteMapping("/comments/unlike")
    public ResponseEntity<CommonResponse> unLikeComment(@Valid @RequestBody UnLikeCommentRequest request) {
        likeCommentService.unLikeComment(request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    /*
    @Operation(summary = "댓글 전체 조회", description = "postId 전달")
    @GetMapping("/posts/{postId}/comment")
    public ResponseEntity<Results<List<Comment>>> getAllComments(@PathVariable("postId") Long postId) {
        List<Comment> comments = commentService.getAllComments(postId);
        return ResponseEntity.ok().body(new Results<>(comments, comments.size()));
    }
    */

    @Operation(summary = "좋아요 한 댓글 조회", description = "userId 전달")
    @GetMapping("/comments/liked/{userId}")
    public ResponseEntity<Results<List<Comment>>> getLikedComments(@PathVariable("userId") Long userId) throws IllegalAccessException {
        List<Comment> comments = commentService.getLikedComment(userId);
        return ResponseEntity.ok().body(new Results<>(comments, comments.size()));
    }
}
