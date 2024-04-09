package bigsanghyuk.four_uni.config.s3.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IcsUploadDto {

    private String imageUrl;

    public IcsUploadDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}