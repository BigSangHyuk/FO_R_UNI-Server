package bigsanghyuk.four_uni.user.dto.request;

import bigsanghyuk.four_uni.user.domain.EditUserInfo;
import bigsanghyuk.four_uni.user.enums.CategoryType;
import lombok.Getter;

@Getter
public class EditUserRequest {

    private CategoryType departmentType;
    private CategoryType departmentTypeSec;
    private String nickName;

    public EditUserInfo toDomain() {
        return new EditUserInfo(departmentType, departmentTypeSec, nickName);
    }
}
