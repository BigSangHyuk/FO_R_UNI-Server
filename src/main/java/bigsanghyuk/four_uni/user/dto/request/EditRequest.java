package bigsanghyuk.four_uni.user.dto.request;

import bigsanghyuk.four_uni.user.domain.EditUserInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class EditRequest {

    @NotNull
    private Long id;
    @NotNull
    private String password;
    @NotNull
    private String name;
    @NotNull
    private int dept;
    @NotNull
    private String nickName;
    @NotNull
    private String image;

    public EditUserInfo toDomain() {
        return new EditUserInfo(id, password, name, dept, nickName, image);
    }
}
