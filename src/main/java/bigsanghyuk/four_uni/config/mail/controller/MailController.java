package bigsanghyuk.four_uni.config.mail.controller;

import bigsanghyuk.four_uni.CommonResponse;
import bigsanghyuk.four_uni.config.mail.dto.SendMailRequest;
import bigsanghyuk.four_uni.config.mail.service.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "emails", description = "메일 인증 API")
public class MailController {

    private final MailService mailService;

    @PostMapping("/auth/send")
    public ResponseEntity<CommonResponse> mailSending(@RequestBody SendMailRequest request) throws Exception {
        Boolean isSentSuccess = mailService.sendCertificationMail(request.toDomain());
        return ResponseEntity.ok().body(new CommonResponse(isSentSuccess));
    }

    @GetMapping("/auth/certificate")
    public ResponseEntity<CommonResponse> mailCertification(@RequestParam(name = "email") String email, @RequestParam(name = "code") String code) {
        Boolean isValid = mailService.validate(email, code);
        return ResponseEntity.ok().body(new CommonResponse(isValid));
    }
}
