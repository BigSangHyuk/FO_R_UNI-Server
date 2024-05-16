package bigsanghyuk.four_uni.user.dto.request;

import bigsanghyuk.four_uni.user.domain.SignUserInfo;
import bigsanghyuk.four_uni.user.enums.CategoryType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class SignRequest {

    @NotNull
    @Email
    private String email;
    @NotNull
    private String password;
    private CategoryType departmentType;
    private CategoryType departmentTypeSub;
    private String nickName;
    private String image;

    public SignUserInfo toDomain() {
        return new SignUserInfo(email, password, departmentType, departmentTypeSub, nickName, image);
    }
}
