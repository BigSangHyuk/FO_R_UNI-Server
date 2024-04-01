package bigsanghyuk.four_uni.config.oauth.handler;

import bigsanghyuk.four_uni.config.jwt.JwtProvider;
import bigsanghyuk.four_uni.config.jwt.dto.TokenDto;
import bigsanghyuk.four_uni.config.oauth.CustomAuthorityUtils;
import bigsanghyuk.four_uni.config.oauth.domain.CustomOAuth2User;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import bigsanghyuk.four_uni.user.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
public class OAuth2UserSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtProvider jwtProvider;
    private final CustomAuthorityUtils authorityUtils;
    private final UserService userService;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getEmail();
        List<Authority> authorities = authorityUtils.createAuthorities(email);
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        String accessToken = createAccessToken(email, user.getId(), authorities);
        String refreshToken = createRefreshToken(email);
        Map<String, TokenDto> tokenDtoMap = makeTokenMap(accessToken, refreshToken);

        String json = toJson(tokenDtoMap);
        responseJson(response, json);
    }

    private String createAccessToken(String email, Long userId, List<Authority> authorities) {
        return jwtProvider.createToken(email, userId, authorities);
    }

    private String createRefreshToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return userService.createRefreshToken(user);
    }

    private Map<String, TokenDto> makeTokenMap(String accessToken, String refreshToken) {
        HashMap<String, TokenDto> map = new HashMap<>();
        TokenDto tokenDto = TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        map.put("token", tokenDto);
        return map;
    }

    private String toJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }

    private void responseJson(HttpServletResponse response, String json) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(json);
        response.getWriter().flush();
    }
}
