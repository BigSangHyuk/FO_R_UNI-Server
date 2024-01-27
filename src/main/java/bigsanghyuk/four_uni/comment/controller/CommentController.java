package bigsanghyuk.four_uni.comment.controller;

import bigsanghyuk.four_uni.comment.dto.request.RegisterCommentRequest;
import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.comment.domain.EditCommentInfo;
import bigsanghyuk.four_uni.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/v1/posts/{postId}/comment")
    public ResponseEntity<CommonResponse> register(@Valid @RequestBody RegisterCommentRequest request) {
        commentService.register(request.toDomain());
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @PutMapping("/v1/posts/{postId}/{commentId}")
    public ResponseEntity<CommonResponse> edit(@PathVariable Long commentId, @Valid @RequestBody EditCommentInfo request) {
        commentService.edit(commentId, request);
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @DeleteMapping("/v1/posts/{postId}/{commentId}")
    public ResponseEntity<CommonResponse> remove(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.remove(postId, commentId);
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

}
