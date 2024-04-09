package bigsanghyuk.four_uni.user.dto.request;

import bigsanghyuk.four_uni.user.domain.EditUserInfo;
import bigsanghyuk.four_uni.user.enums.CategoryType;
import lombok.Getter;

@Getter
public class EditUserRequest {

    private CategoryType departmentType;
    private String nickName;
    private String image;

    public EditUserInfo toDomain() {
        return new EditUserInfo(departmentType, nickName, image);
    }
}
