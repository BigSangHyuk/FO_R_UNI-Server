package bigsanghyuk.four_uni.user.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore //@ResponseBody 에 의해 객체를 JSON 으로 변경시 @JsonIgnore 가 붙은 변수는 무시한 채 직렬화
    private Long id;
    private String name;
    @JoinColumn(name = "authUser")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User authUser;

    public void setUser(User authUser) {
        this.authUser = authUser;
    }
}
