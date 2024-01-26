package bigsanghyuk.four_uni.controller;

import bigsanghyuk.four_uni.controller.request.CommentRegisterRequest;
import bigsanghyuk.four_uni.controller.response.CommonResponse;
import bigsanghyuk.four_uni.domain.CommentEditInfo;
import bigsanghyuk.four_uni.service.CommentService;
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
    public ResponseEntity<CommonResponse> register(@Valid @RequestBody CommentRegisterRequest request) {
        commentService.register(request.toDomain());
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @PutMapping("/v1/posts/{postId}/{commentId}")
    public ResponseEntity<CommonResponse> edit(@PathVariable Long commentId, @Valid @RequestBody CommentEditInfo request) {
        commentService.edit(commentId, request);
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @DeleteMapping("/v1/posts/{postId}/{commentId}")
    public ResponseEntity<CommonResponse> remove(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.remove(postId, commentId);
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

}
