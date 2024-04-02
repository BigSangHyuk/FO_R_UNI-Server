package bigsanghyuk.four_uni.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LogoutUserInfo {

    private String accessToken;
    private String refreshToken;
}
