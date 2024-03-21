package bigsanghyuk.four_uni.config.mail.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.config.mail.dto.SendMailRequest;
import bigsanghyuk.four_uni.config.mail.service.MailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "emails", description = "메일 인증 API")
public class MailController {

    private final MailService mailService;

    @Operation(summary = "메일 전송", description = "바디에 email 담아서 요청")
    @PostMapping("/auth/send")
    public ResponseEntity<CommonResponse> mailSending(@RequestBody SendMailRequest request) throws Exception {
        Boolean isSentSuccess = mailService.sendCertificationMail(request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(isSentSuccess));
    }

    @Operation(summary = "인증번호 검증", description = "쿼리 파라미터로 이메일, 코드 입력")
    @GetMapping("/auth/certificate")
    public ResponseEntity<CommonResponse> mailCertification(@RequestParam(name = "email") String email, @RequestParam(name = "code") String code) {
        Boolean isValid = mailService.validate(email, code);
        return ResponseEntity.ok().body(new CommonResponse(isValid));
    }
}
