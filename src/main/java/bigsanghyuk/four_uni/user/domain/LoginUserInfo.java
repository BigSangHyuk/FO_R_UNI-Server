package bigsanghyuk.four_uni.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginUserInfo {

    private String email;
    private String password;
}
