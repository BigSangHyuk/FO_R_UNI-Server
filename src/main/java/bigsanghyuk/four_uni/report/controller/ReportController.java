package bigsanghyuk.four_uni.report.controller;

import bigsanghyuk.four_uni.common.CommonResponse;
import bigsanghyuk.four_uni.report.dto.request.CorrectDeadlineRequest;
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

    @Operation(summary = "댓글 신고", description = "요청에 commentId, reason, detail 넣어서")
    @PostMapping("/comment")
    public ResponseEntity<CommonResponse> reportComment(@RequestAttribute(name = "userId") Long userId, @RequestBody ReportCommentRequest request) {
        boolean result = reportService.reportComment(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(result));
    }

    @Operation(summary = "게시글 신고", description = "요청에 postId, reason, detail 넣어서")
    @PostMapping("/post")
    public ResponseEntity<CommonResponse> reportPost(@RequestAttribute(name = "userId") Long userId, @RequestBody ReportPostRequest request) {
        boolean result = reportService.reportPost(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(result));
    }

    @Operation(summary = "마감날짜 수정 요청", description = "요청에 postId, deadline(yyyy-MM-dd) 넣어서")
    @PostMapping("/deadline")
    public ResponseEntity<CommonResponse> reportDeadline(@RequestAttribute(name = "userId") Long userId, @RequestBody CorrectDeadlineRequest request) {
        boolean result = reportService.reportDeadline(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(result));
    }
}
