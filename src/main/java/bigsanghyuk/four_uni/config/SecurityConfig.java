package bigsanghyuk.four_uni.config;

import bigsanghyuk.four_uni.config.filter.CorsFilter;
import bigsanghyuk.four_uni.config.filter.JwtAuthenticationFilter;
import bigsanghyuk.four_uni.config.filter.JwtExceptionFilter;
import bigsanghyuk.four_uni.config.interceptor.LogInterceptor;
import bigsanghyuk.four_uni.config.interceptor.TokenInterceptor;
import bigsanghyuk.four_uni.config.jwt.JwtProvider;
import bigsanghyuk.four_uni.config.oauth.CustomAuthorityUtils;
import bigsanghyuk.four_uni.config.oauth.handler.OAuth2UserSuccessHandler;
import bigsanghyuk.four_uni.config.oauth.service.CustomOAuth2UserService;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import bigsanghyuk.four_uni.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements WebMvcConfigurer {

    private final JwtProvider jwtProvider;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomAuthorityUtils authorityUtils;
    private final UserService userService;
    private final UserRepository userRepository;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(HttpBasicConfigurer::disable)
                //JWT 기반이므로 쿠키 사용 X
                .csrf(CsrfConfigurer::disable)
                .cors(c -> {
                            CorsConfigurationSource source = request -> {
                                CorsConfiguration config = new CorsConfiguration();
                                config.setAllowedOrigins(
                                        List.of("*")
                                );
                                config.setAllowedMethods(
                                        List.of("*")
                                );
                                return config;
                            };
                            c.configurationSource(source);
                        }
                )
                // 세션 생성 X, 사용 X
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 조건마다 요청 허용, 제한 설정
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/sign-up", "/sign-in", "/refresh", "/swagger-ui/**", "/v3/api-docs/**", "/auth/**", "/healthcheck").permitAll()
                        .requestMatchers("/admins/**", "/add-post/**").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/users/**", "/posts/**", "/comments/**", "/reports/**", "/s3/**", "/fcm/**", "/emails/**", "/log-out", "/leave").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .anyRequest().denyAll())
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(new OAuth2UserSuccessHandler(jwtProvider, authorityUtils, userService, userRepository))
                        .userInfoEndpoint(
                                userInfoEndpointConfig -> userInfoEndpointConfig
                                        .userService(customOAuth2UserService)
                        )
                )
                .addFilterBefore(new CorsFilter(), ChannelProcessingFilter.class)
                // 인증 필터
                .addFilterBefore(new JwtAuthenticationFilter(jwtProvider), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JwtExceptionFilter(), JwtAuthenticationFilter.class)
                // 에러 핸들링
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "인증되지 않은 사용자입니다.");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "권한이 없습니다.");
                        })
                );
        return http.build();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/refresh",
                        "/healthcheck"
                );
        registry.addInterceptor(new TokenInterceptor(jwtProvider))
                .excludePathPatterns(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/auth/**",
                        "/sign-up",
                        "/sign-in",
                        "/refresh",
                        "/healthcheck"
                );
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
