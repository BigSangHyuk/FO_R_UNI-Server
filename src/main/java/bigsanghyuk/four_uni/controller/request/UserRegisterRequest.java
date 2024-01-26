package bigsanghyuk.four_uni.controller.request;


import bigsanghyuk.four_uni.domain.UserRegisterInfo;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

@Data
public class UserRegisterRequest {

    @NotNull
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

    public UserRegisterInfo toDomain() {
        return new UserRegisterInfo(email, password, name, dept, nickName, image);
    }
}
