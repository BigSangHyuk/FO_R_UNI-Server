package bigsanghyuk.four_uni.controller.request;

import bigsanghyuk.four_uni.domain.UpdateUserInfo;
import bigsanghyuk.four_uni.domain.UserRegisterInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequest {

    private Long id;
    private String password;
    private String nickName;
    private String image;

    public UpdateUserInfo toDomain() {
        return new UpdateUserInfo(id, password, nickName, image);
    }

}
