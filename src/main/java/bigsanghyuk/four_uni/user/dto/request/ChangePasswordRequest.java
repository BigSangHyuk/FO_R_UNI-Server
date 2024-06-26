package bigsanghyuk.four_uni.user.dto.request;

import bigsanghyuk.four_uni.user.domain.ChangePasswordInfo;
import lombok.Getter;

@Getter
public class ChangePasswordRequest {

    private String oldPassword;
    private String newPassword;

    public ChangePasswordInfo toDomain() {
        return new ChangePasswordInfo(oldPassword, newPassword);
    }
}
