package bigsanghyuk.four_uni.config.s3.controller;

import bigsanghyuk.four_uni.config.s3.dto.ImageUploadDto;
import bigsanghyuk.four_uni.config.s3.service.S3Uploader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "images", description = "S3 업로드 API")
public class S3Controller {

    private final S3Uploader s3Uploader;

    @Operation(summary = "이미지 업로드", description = "multipart/form-data 전송, 저장된 URL 반환")
    @PostMapping(value = "/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> upload(@RequestParam(name = "images") MultipartFile multipartFile) {
        try {
            String uploadedUrl = s3Uploader.upload(multipartFile, "static");
            return ResponseEntity.ok().body(new ImageUploadDto(uploadedUrl, "이미지 업로드에 성공했습니다."));
        } catch (Exception e) {
            log.error("Error occurred while uploading image: ", e);
            return ResponseEntity.badRequest().body("잘못된 요청입니다.");
        }
    }
}
