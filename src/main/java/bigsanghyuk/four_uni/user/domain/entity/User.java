package bigsanghyuk.four_uni.user.domain.entity;

import bigsanghyuk.four_uni.config.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter @Setter
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
    private String name;
    private int dept;
    private String nickName;
    private String image;

    @OneToMany(mappedBy = "authUser", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public User(String email, String password, String name, int dept, String nickName, String image) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.dept = dept;
        this.nickName = nickName;
        this.image = image;
    }

    public void edit(String password, String name, int dept, String nickName, String image) {
        this.password = password;
        this.name = name;
        this.dept = dept;
        this.nickName = nickName;
        this.image = image;
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

    public User updateName(String name) {
        this.name = name;
        return this;
    }
}
