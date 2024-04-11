package bigsanghyuk.four_uni.config.jwt.dto.request;

import bigsanghyuk.four_uni.config.jwt.domain.AccessTokenReissueInfo;
import lombok.Getter;

@Getter
public class AccessTokenReissueRequest {

    private Long userId;
    private String refreshToken;

    public AccessTokenReissueInfo toDomain() {
        return new AccessTokenReissueInfo(userId, refreshToken);
    }
}
