package bigsanghyuk.four_uni.config.fcm.dto;

import lombok.*;

@Getter @ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor (access = AccessLevel.PROTECTED)
public class FcmSendDto {   // 모바일에서 전달받은 객체

    private String token;
    private String title;
    private String body;
}
