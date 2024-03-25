package bigsanghyuk.four_uni.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePasswordInfo {

    private Long id;
    private String oldPassword;
    private String newPassword;
}
