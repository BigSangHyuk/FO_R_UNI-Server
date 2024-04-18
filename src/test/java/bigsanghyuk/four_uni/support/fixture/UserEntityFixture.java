package bigsanghyuk.four_uni.support.fixture;

import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.enums.CategoryType;

import java.util.Collections;
import java.util.List;

public enum UserEntityFixture {
    USER_NORMAL("test_email@test.com", "testPassword", CategoryType.ISIS, "testNickName", "testImageUrl", Collections.singletonList(Authority.builder().name("ROLE_USER").build())),
    USER_ADMIN("test_email@test.com2", "testPassword2", CategoryType.ISIS, "testNickName2", "testImageUrl2", Collections.singletonList(Authority.builder().name("ROLE_ADMIN").build()));

    private final String email;
    private final String password;
    private final CategoryType departmentType;
    private final String nickName;
    private final String image;
    private final List<Authority> roles;

    UserEntityFixture(String email, String password, CategoryType departmentType, String nickName, String image, List<Authority> roles) {
        this.email = email;
        this.password = password;
        this.departmentType = departmentType;
        this.nickName = nickName;
        this.image = image;
        this.roles = roles;
    }

    public User UserEntity_생성(Long id) {
        return User.builder()
                .id(id)
                .email(this.email)
                .password(this.password)
                .departmentType(this.departmentType)
                .nickName(this.nickName)
                .image(this.image)
                .roles(this.roles)
                .build();
    }

    public User UserEntity_생성() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .departmentType(this.departmentType)
                .nickName(this.nickName)
                .image(this.image)
                .roles(this.roles)
                .build();
    }
}
