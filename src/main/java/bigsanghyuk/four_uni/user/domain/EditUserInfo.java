package bigsanghyuk.four_uni.user.domain;

import bigsanghyuk.four_uni.user.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditUserInfo {

    private String password;
    private String name;
    private CategoryType departmentType;
    private String nickName;
    private String image;
}
