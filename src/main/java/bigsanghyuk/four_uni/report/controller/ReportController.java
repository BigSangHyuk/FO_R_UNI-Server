package bigsanghyuk.four_uni.report.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.report.dto.request.ReportCommentRequest;
import bigsanghyuk.four_uni.report.dto.request.ReportPostRequest;
import bigsanghyuk.four_uni.report.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
@Tag(name = "reports", description = "신고 API")
public class ReportController {

    private final ReportService reportService;

    @Operation(summary = "댓글 신고", description = "요청에 userId, commentId, reason, detail 넣어서")
    @PostMapping("/comment")
    public ResponseEntity<CommonResponse> reportComment(@RequestAttribute(name = "userId") Long userId, @RequestBody ReportCommentRequest request) {
        reportService.reportComment(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }

    @Operation(summary = "게시글 신고", description = "요청에 userId, postId, reason, detail 넣어서")
    @PostMapping("/post")
    public ResponseEntity<CommonResponse> reportPost(@RequestAttribute(name = "userId") Long userId, @RequestBody ReportPostRequest request) {
        reportService.reportPost(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }
}
