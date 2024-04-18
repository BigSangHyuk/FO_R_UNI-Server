package bigsanghyuk.four_uni.config.s3.controller;

import bigsanghyuk.four_uni.config.s3.dto.IcsUploadDto;
import bigsanghyuk.four_uni.config.s3.service.S3Uploader;
import bigsanghyuk.four_uni.post.service.ICalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "s3", description = "S3 업로드 API")
@RequestMapping("/s3")
public class S3Controller {

    private final S3Uploader s3Uploader;
    private final ICalService iCalService;

    @Operation(summary = "ics 업로드", description = "저장할 postId 입력하면 ics 저장된 url 반환")
    @PostMapping(value = "/ics")
    public ResponseEntity<?> upload(@RequestParam(name = "postid") Long postId) {
        try {
            byte[] data = iCalService.getEventInfo(postId);
            String uploadedUrl = s3Uploader.upload(data, "ics");
            return ResponseEntity.ok().body(new IcsUploadDto(uploadedUrl));
        } catch (IOException e) {
            log.error("Error occurred while uploading ics: ", e);
            throw new RuntimeException(e);
        }
    }
}
