package bigsanghyuk.four_uni.config.jwt.dto.request;

import lombok.Getter;

@Getter
public class RefreshAccessTokenRequest {

    private String refreshToken;
}
