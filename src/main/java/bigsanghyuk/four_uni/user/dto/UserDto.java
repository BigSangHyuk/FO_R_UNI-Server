package bigsanghyuk.four_uni.user.dto;

import bigsanghyuk.four_uni.user.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @Builder
@NoArgsConstructor
public class UserDto {

    private Long userId;
    private String email;
    private String nickName;
    private String image;

    private UserDto(Long userId, String email, String nickName, String image) {
        this.userId = userId;
        this.email = email;
        this.nickName = nickName;
        this.image = image;
    }

    public static UserDto of(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getNickName(),
                user.getImage()
        );
    }
}
