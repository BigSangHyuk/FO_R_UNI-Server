package bigsanghyuk.four_uni.config.oauth;

import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String nameAttributeKey = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, nameAttributeKey, oAuth2User.getAttributes());

        saveOrUpdate(attributes);
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                attributes.getAttributes(),
                attributes.getNameAttributeKey()
        );
    }

    private void saveOrUpdate(OAuthAttributes attributes) {
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
                        .name(attributes.getName())
                        .build());
        userRepository.save(user);
    }
}
