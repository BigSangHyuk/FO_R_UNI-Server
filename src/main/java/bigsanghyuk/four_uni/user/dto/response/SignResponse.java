package bigsanghyuk.four_uni.user.dto.response;

import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignResponse {

    private Long id;
    private String email;
    private String department;
    private String departmentSec;
    private Integer deptId;
    private String nickName;
    private String image;
    private List<Authority> roles = new ArrayList<>();
}
