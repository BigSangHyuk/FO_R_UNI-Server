package bigsanghyuk.four_uni.user.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter @Setter
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String email;
    private String password;
    private String name;
    private int dept;
    private String nickName;
    private String image;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

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
