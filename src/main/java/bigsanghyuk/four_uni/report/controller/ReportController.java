package bigsanghyuk.four_uni.report.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.comment.dto.request.ReportCommentRequest;
import bigsanghyuk.four_uni.report.service.ReportService;
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
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/comments/report/{userId}/{commentId}")
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
}
