package bigsanghyuk.four_uni.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditUserInfo {

    private Long id;
    private String password;
    private String name;
    private int dept;
    private String nickName;
    private String image;
}
