package bigsanghyuk.four_uni.config.fcm.controller;

import bigsanghyuk.four_uni.common.CommonResponse;
import bigsanghyuk.four_uni.config.fcm.dto.FcmSendDto;
import bigsanghyuk.four_uni.config.fcm.service.FcmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Tag(name = "FCM", description = "Push 알림 API")
public class FcmController {

    private final FcmService fcmService;

    @Operation(summary = "push 메시지 전송", description = "fcm 토큰, 제목, 내용 담아서 전달")
    @PostMapping("/fcm/send")
    public ResponseEntity<CommonResponse> pushMessage(@RequestBody FcmSendDto fcmSendDto) throws IOException {
        boolean result = fcmService.sendMessageTo(fcmSendDto);
        return ResponseEntity.ok().body(new CommonResponse(result));
    }
}
