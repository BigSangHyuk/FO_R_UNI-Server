package bigsanghyuk.four_uni.user.domain.entity;

import bigsanghyuk.four_uni.user.domain.UpdateUserInfo;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Builder
@Getter @Setter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
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

    public User(Long id, String email, String password, String name, int dept, String nickName, String image, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.dept = dept;
        this.nickName = nickName;
        this.image = image;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(String email, String password, String name, int dept, String nickName, String image) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.dept = dept;
        this.nickName = nickName;
        this.image = image;
        this.createdAt = LocalDateTime.now();
    }

    public User(Long id, String password, String nickName, String image) {
        this.id = id;
        this.password = password;
        this.nickName =nickName;
        this.image = image;
    }

    public void edit(@Valid UpdateUserInfo updateUserInfo) {
        this.password = updateUserInfo.getPassword();
        this.nickName = updateUserInfo.getNickName();
        this.image = updateUserInfo.getImage();
    }
}
