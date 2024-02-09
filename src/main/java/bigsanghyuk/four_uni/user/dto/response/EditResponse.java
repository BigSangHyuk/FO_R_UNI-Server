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
public class EditResponse {

    private String email;
    private String name;
    private int dept;
    private String nickName;
    private String image;
    private List<Authority> roles = new ArrayList<>();

    public EditResponse(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.dept = user.getDept();
        this.nickName = user.getNickName();
        this.image = user.getImage();
        this.roles = user.getRoles();
    }
}
