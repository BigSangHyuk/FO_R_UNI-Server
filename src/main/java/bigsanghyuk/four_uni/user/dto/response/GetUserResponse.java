package bigsanghyuk.four_uni.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GetUserResponse {

    private String password;
    private String nickName;
    private String image;

    public GetUserResponse(String password, String nickName, String image) {
        this.password = password;
        this.nickName = nickName;
        this.image = image;
    }
}
