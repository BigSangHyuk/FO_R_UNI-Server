package bigsanghyuk.four_uni.report.controller;

import bigsanghyuk.four_uni.CommonResponse;
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
    @PostMapping("/comments/report/{userId}/{commentId}")
    public ResponseEntity<String> reportComment(
            @PathVariable("commentId") Long commentId,
            @RequestBody Map<String, String> payload
    ) {
        String reportReasonString = payload.get("reportReason");
        reportService.reportComment(commentId, userId, reportReasonString);

        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }

    @Operation(summary = "게시글 신고", description = "userId와 postId 전달")
    @PostMapping("/posts/report/{userId}/{postId}")
    public ResponseEntity<String> reportPost(
            @PathVariable("postId") Long postId,
            @RequestBody Map<String, String> payload
    ) {
        String reportReasonString = payload.get("reportReason");
        reportService.reportPost(postId, userId, reportReasonString);

        return new ResponseEntity(new CommonResponse(true), HttpStatus.OK);
    }
}
