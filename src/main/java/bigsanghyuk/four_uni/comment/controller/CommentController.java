package bigsanghyuk.four_uni.comment.controller;

import bigsanghyuk.four_uni.common.CommonResponse;
import bigsanghyuk.four_uni.common.Results;
import bigsanghyuk.four_uni.comment.domain.entity.CommentRequired;
import bigsanghyuk.four_uni.comment.dto.request.*;
import bigsanghyuk.four_uni.comment.dto.request.EditCommentRequest;
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

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "comments", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;
    private final LikeCommentService likeCommentService;

    @Operation(summary = "댓글 등록", description = "Body에 글 id, 부모 댓글 id (nullable), 댓글 내용 전달")
    @PostMapping("/comments")
    public ResponseEntity<CommonResponse> register(@RequestAttribute(name = "userId") Long userId, @Valid @RequestBody RegisterCommentRequest request) {
        commentService.write(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "댓글 수정", description = "Body에 postId, commentId, 수정 내용 넣어서 전달")
    @PutMapping("/comments")
    public ResponseEntity<CommonResponse> edit(@RequestAttribute(name = "userId") Long userId, @Valid @RequestBody EditCommentRequest request) {
        commentService.edit(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "댓글 삭제", description = "Body에 postId, commentId 넣어서 전달")
    @DeleteMapping("/comments")
    public ResponseEntity<CommonResponse> remove(@RequestAttribute(name = "userId") Long userId, @Valid @RequestBody DeleteCommentRequest request) {
        commentService.remove(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "댓글 좋아요", description = "Body에 commentId 넣어서 전달")
    @PostMapping("/comments/like/{commentId}")
    public ResponseEntity<CommonResponse> likeComment(@RequestAttribute(name = "userId") Long userId, @PathVariable(name = "commentId") Long commentId) {
        likeCommentService.likeComment(userId, commentId);
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "댓글 좋아요 취소", description = "Body에 commentId 넣어서 전달")
    @DeleteMapping("/comments/unlike/{commentId}")
    public ResponseEntity<CommonResponse> unLikeComment(@RequestAttribute(name = "userId") Long userId, @PathVariable(name = "commentId") Long commentId) {
        likeCommentService.unLikeComment(userId, commentId);
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "좋아요 한 댓글 조회", description = "최소 정보")
    @GetMapping("/comments/liked")
    public ResponseEntity<Results<List<CommentRequired>>> getLikedCommentsRequiredData(@RequestAttribute(name = "userId") Long userId) {
        List<CommentRequired> comments = commentService.getLikedComment(userId);
        return ResponseEntity.ok().body(new Results<>(comments, comments.size()));
    }
}
