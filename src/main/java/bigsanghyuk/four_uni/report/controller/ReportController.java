package bigsanghyuk.four_uni.report.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.comment.dto.request.ReportCommentRequest;
import bigsanghyuk.four_uni.post.dto.request.ReportPostRequest;
import bigsanghyuk.four_uni.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "reports", description = "신고 API")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "댓글 신고", description = "userId와 commentId 전달")
    @PostMapping("/v1/comments/report/{userId}/{commentId}")
    public ResponseEntity<String> reportComment(
            @PathVariable("commentId") Long commentId,
            @PathVariable("userId") Long userId,
            @RequestBody Map<String, String> payload
    ) {
        try {
            String reportReasonString = payload.get("reportReason");
            ReportCommentRequest reportCommentDto = reportService.reportComment(commentId, userId, reportReasonString);

            if (reportCommentDto != null) {
                return ResponseEntity.ok("댓글이 성공적으로 신고되었습니다!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 신고에 실패했습니다.");
        }
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @Operation(summary = "게시글 신고", description = "userId와 postId 전달")
    @PostMapping("/v1/posts/report/{userId}/{postId}")
    public ResponseEntity<String> reportPost(
            @PathVariable("postId") Long postId,
            @PathVariable("userId") Long userId,
            @RequestBody Map<String, String> payload
    ) {
        try {
            String reportReasonString = payload.get("reportReason");
            ReportPostRequest reportPostDto = reportService.reportPost(postId, userId, reportReasonString);

            if (reportPostDto != null) {
                return ResponseEntity.ok("게시글이 성공적으로 신고되었습니다!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시글 신고에 실패했습니다.");
        }
        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }
}
