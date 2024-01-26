package bigsanghyuk.four_uni.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserRegisterInfo {
    private Long id;
    private String email;
    private String password;
    private String name;
    private int dept;
    private String nickName;
    private String image;
}

