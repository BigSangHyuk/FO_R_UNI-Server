package bigsanghyuk.four_uni.config.oauth.service;

import bigsanghyuk.four_uni.config.oauth.CustomAuthorityUtils;
import bigsanghyuk.four_uni.config.oauth.OAuthAttributes;
import bigsanghyuk.four_uni.config.oauth.domain.CustomOAuth2User;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final CustomAuthorityUtils authorityUtils;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> originAttributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, originAttributes);
        User user = saveOrUpdate(attributes);
        String email = user.getEmail();
        List<Authority> authorities = authorityUtils.createAuthorities(email);
        user.setRoles(authorities);
        userRepository.save(user);

        return new CustomOAuth2User(registrationId, originAttributes, authorities, email);
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        String email = attributes.getEmail();
        User user = userRepository.findByEmail(email)
                .map(entity -> entity
                        .updateNickName(attributes.getNickName())
                        .updateImage(attributes.getImage())
                        .updateName(attributes.getName()))
                .orElse(User.builder()
                        .email(email)
                        .nickName(attributes.getNickName())
                        .image(attributes.getImage())
                        .dept(-1)   //null 이라고 생각
                        .name(attributes.getName())
                        .build());
        return userRepository.save(user);
    }
}
