package bigsanghyuk.four_uni.config.oauth.domain;

import bigsanghyuk.four_uni.user.domain.entity.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class CustomOAuth2User implements OAuth2User, Serializable {

    private String registrationId;
    private Map<String, Object> attributes;
    @Getter
    private List<Authority> roles;
    @Getter
    private String email;

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getName() {
        return this.registrationId;
    }
}
