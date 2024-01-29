package bigsanghyuk.four_uni.user.dto.request;

import bigsanghyuk.four_uni.user.domain.LoginUserInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginUserRequest {

    @NotNull @Email
    private String email;
    @NotNull
    private String password;

    public LoginUserInfo toDomain() {
        return new LoginUserInfo(email, password);
    }
}
