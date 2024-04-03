package bigsanghyuk.four_uni.common.controller;

import bigsanghyuk.four_uni.common.CommonResponse;
import bigsanghyuk.four_uni.post.scheduler.PostScheduler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "test", description = "테스트 API")
public class TestController {

    private final PostScheduler postScheduler;

    @Operation(summary = "게시글 받아오기 테스트", description = "스케쥴러 메소드 강제 실행, 관리자 아이디만")
    @GetMapping("/add-post/test")
    public ResponseEntity<CommonResponse> registerPost() {
        postScheduler.getPostData();
        return ResponseEntity.ok().body(new CommonResponse(true));
    }
}
