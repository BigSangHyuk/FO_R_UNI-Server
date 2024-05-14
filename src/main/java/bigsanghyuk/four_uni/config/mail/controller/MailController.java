package bigsanghyuk.four_uni.config.mail.controller;

import bigsanghyuk.four_uni.common.CommonResponse;
import bigsanghyuk.four_uni.config.mail.dto.InquireMailRequest;
import bigsanghyuk.four_uni.config.mail.dto.SendMailRequest;
import bigsanghyuk.four_uni.config.mail.service.MailService;
import bigsanghyuk.four_uni.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "emails", description = "메일 인증 API")
public class MailController {

    private final MailService mailService;
    private final UserService userService;

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

    @Operation(summary = "임시 비밀번호 발급", description = "바디에 email 담아서 요청")
    @PostMapping("/auth/temp-pw")
    public ResponseEntity<CommonResponse> tempPassword(@RequestBody SendMailRequest request) {
        Boolean sendPw = userService.setToTempPassword(request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(sendPw));
    }

    @Operation(summary = "문의하기", description = "바디에 문의사항 담아서 요청")
    @PostMapping("/emails/inquiry")
    public ResponseEntity<CommonResponse> ask(@RequestAttribute(name = "userId") Long userId, @RequestBody InquireMailRequest request) throws MessagingException {
        mailService.sendMail(userId, request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(true));
    }
}
