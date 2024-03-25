package bigsanghyuk.four_uni.user.dto.response;

import bigsanghyuk.four_uni.department.domain.entity.Department;
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
    private String password;
    private String name;
    private Department dept;
    private String nickName;
    private String image;
    private List<Authority> roles = new ArrayList<>();

    public SignResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.password = user.getPassword();
        this.name = user.getName();
        this.dept = user.getDept();
        this.nickName = user.getNickName();
        this.image = user.getImage();
        this.roles = user.getRoles();
    }
}
