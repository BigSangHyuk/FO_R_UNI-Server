package bigsanghyuk.four_uni.config.s3.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ImageUploadDto {

    private String imageUrl;
    private String message;

    public ImageUploadDto(String imageUrl, String message) {
        this.imageUrl = imageUrl;
        this.message = message;
    }
}