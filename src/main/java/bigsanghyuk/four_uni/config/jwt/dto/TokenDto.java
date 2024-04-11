package bigsanghyuk.four_uni.config.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @AllArgsConstructor
@Builder @NoArgsConstructor
public class TokenDto {

    private Long userId;
    private String accessToken;
    private String refreshToken;
}
