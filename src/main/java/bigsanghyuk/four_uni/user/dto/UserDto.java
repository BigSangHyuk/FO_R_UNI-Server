package bigsanghyuk.four_uni.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter @Builder
public class UserDto {

    private Long userId;
    private String email;
    private String nickName;
    private String image;
}
