package bigsanghyuk.four_uni.user.dto.response;

import bigsanghyuk.four_uni.config.jwt.dto.TokenDto;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {

    private Long id;
    private String email;
    private String department;
    private String departmentSub;
    private Integer deptId;
    private String nickName;
    private String image;
    private List<Authority> roles = new ArrayList<>();
    private TokenDto token;
}
