package bigsanghyuk.four_uni.config.jwt.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AccessTokenReissueInfo {

    private Long userId;
    private String refreshToken;
}
