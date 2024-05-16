package bigsanghyuk.four_uni.user.domain;

import bigsanghyuk.four_uni.user.enums.CategoryType;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditUserInfo {

    private CategoryType departmentType;
    private CategoryType departmentTypeSub;
    private String nickName;
}
