package bigsanghyuk.four_uni.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateUserInfo {

    private Long id;
    private String password;
    private String nickName;
    private String image;
}
