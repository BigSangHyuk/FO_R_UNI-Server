package bigsanghyuk.four_uni.config.oauth;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String nickName;
    private String email;
    private String image;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String nickName, String email, String image) {
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.nickName = nickName;
        this.email = email;
        this.image = image;
    }

    public static OAuthAttributes of(String registrationId, Map<String, Object> attributes) {
        if ("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        } else if ("kakao".equals(registrationId)) {
            return ofKakao("id", attributes);
        } else {
            return ofGoogle("sub", attributes);
        }
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .nickName((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .image((String) attributes.get("picture"))
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuthAttributes.builder()
                .attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .nickName((String) response.get("nickname"))
                .email((String) response.get("email"))
                .image((String) response.get("profile_image"))
                .build();
    }

    public static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
        return OAuthAttributes.builder()
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .nickName((String) kakaoProfile.get("nickname"))
                .email((String) kakaoAccount.get("email"))
                .image((String) kakaoProfile.get("profile_image_url"))
                .build();
    }
}
