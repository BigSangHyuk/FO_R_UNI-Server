package bigsanghyuk.four_uni.user.domain.entity;

import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
import bigsanghyuk.four_uni.user.enums.CategoryType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity @Builder @Getter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true) @Email
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "department")
    private CategoryType departmentType;
    @Enumerated(EnumType.STRING)
    @Column(name = "department_sub")
    private CategoryType departmentTypeSub;

    private String nickName;
    private String image;

    @OneToMany(mappedBy = "authUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void edit(CategoryType departmentType, String nickName) {
        this.departmentType = departmentType;
        this.nickName = nickName;
    }

    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setUser(this));
    }

    public User updateNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }

    public User updateImage(String image) {
        this.image = image;
        return this;
    }

    public User updatePassword(String password) {   // 임시 비밀번호 발급이나 비밀번호 변경시에만 사용
        this.password = password;
        return this;
    }
}
