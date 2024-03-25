package bigsanghyuk.four_uni.user.domain;

import bigsanghyuk.four_uni.department.domain.entity.Department;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EditUserInfo {

    private Long id;
    private String password;
    private String name;
    private Department dept;
    private String nickName;
    private String image;
}
