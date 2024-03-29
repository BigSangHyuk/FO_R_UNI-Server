package bigsanghyuk.four_uni.config.oauth.handler;

import bigsanghyuk.four_uni.config.jwt.JwtProvider;
import bigsanghyuk.four_uni.config.oauth.CustomAuthorityUtils;
import bigsanghyuk.four_uni.config.oauth.domain.CustomOAuth2User;
import bigsanghyuk.four_uni.exception.user.UserNotFoundException;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import bigsanghyuk.four_uni.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

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

        redirect(request, response, email, authorities);
    }

    private void redirect(HttpServletRequest request, HttpServletResponse response, String email, List<Authority> authorities) throws IOException {
        log.info("creating Token");

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        String accessToken = createAccessToken(email, user.getId(), authorities);
        String refreshToken = createRefreshToken(email);

        String uri = createURI(accessToken, refreshToken, email).toString();
        getRedirectStrategy().sendRedirect(request, response, uri);
    }

    private String createAccessToken(String email, Long userId, List<Authority> authorities) {
        return jwtProvider.createToken(email, userId, authorities);
    }

    private String createRefreshToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
        return userService.createRefreshToken(user);
    }

    private URI createURI(String accessToken, String refreshToken, String email) {
        MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        queryParams.add("email", email);
        queryParams.add("access_token", accessToken);
        queryParams.add("refresh_token", refreshToken);

        return UriComponentsBuilder
                .newInstance()
                .scheme("http")
                .host("fouruni.app")
                .path("/oauth")
                .queryParams(queryParams)
                .build()
                .toUri();
    }
}
