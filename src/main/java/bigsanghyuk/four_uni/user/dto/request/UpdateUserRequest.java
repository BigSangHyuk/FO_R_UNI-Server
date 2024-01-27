package bigsanghyuk.four_uni.user.dto.request;

import bigsanghyuk.four_uni.user.domain.UpdateUserInfo;
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
