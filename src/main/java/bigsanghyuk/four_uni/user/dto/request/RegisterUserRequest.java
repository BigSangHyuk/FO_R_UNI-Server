package bigsanghyuk.four_uni.user.dto.request;

import bigsanghyuk.four_uni.user.domain.RegisterUserInfo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor
@Getter
public class RegisterUserRequest {

    @NotNull @Email
    private String email;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private int dept;
    @NotNull
    private String nickName;

    private String image;

    public RegisterUserInfo toDomain() {
        return new RegisterUserInfo(email, password, name, dept, nickName, image);
    }
}
