package bigsanghyuk.four_uni.user.dto.request;

import bigsanghyuk.four_uni.user.domain.LogoutUserInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class LogoutUserRequest {

    private String accessToken;
    private String refreshToken;

    public LogoutUserInfo toDomain() {
        return new LogoutUserInfo(accessToken, refreshToken);
    }
}
