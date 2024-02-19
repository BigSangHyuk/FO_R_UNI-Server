package bigsanghyuk.four_uni.config.oauth;

import bigsanghyuk.four_uni.user.domain.entity.Authority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class CustomAuthorityUtils {

    private static List<String> adminMailAddress = List.of("fouruni2024@gmail.com", "fouruni19@gmail.com");

    private final List<Authority> ADMIN_ROLES = Collections.singletonList(Authority.builder().name("ROLE_ADMIN").build());
    private final List<Authority> USER_ROLES = Collections.singletonList(Authority.builder().name("ROLE_USER").build());

    public List<Authority> createAuthorities(String email) {
        return (adminMailAddress.contains(email)) ? ADMIN_ROLES : USER_ROLES;
    }
}
