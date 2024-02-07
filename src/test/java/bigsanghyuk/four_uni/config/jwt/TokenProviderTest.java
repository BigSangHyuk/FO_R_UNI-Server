package bigsanghyuk.four_uni.config.jwt;

import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@PropertySource("classpath:/application-jwt.properties")
@Slf4j
class TokenProviderTest {

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.secret}")
    private String secretKey;


    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    UserRepository userRepository;

    @Test
    void generateToken() {
        User testUser = userRepository.save(User.builder()
                .id(100L)
                .email("test@test.com")
                .password("test")
                .name("test")
                .dept(10)
                .nickName("test")
                .image("test")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );
        String token = tokenProvider.generateToken(testUser.getId(), 43200);
        Long userId = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .get("userId", Long.class);
        log.info("token={}", token);
        assertThat(userId).isEqualTo(testUser.getId());
    }

    @Test
    void isNotValidToken() {
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(50).toMillis()))
                .build()
                .createToken(issuer, secretKey);
        boolean result = tokenProvider.validToken(token);
        assertThat(result).isFalse();
    }

    @Test
    void getAuthentication() {
        String testUserEmail = "testUser@email.com";
        String token = JwtFactory.builder()
                .subject(testUserEmail)
                .build()
                .createToken(issuer, secretKey);
        Authentication authentication = tokenProvider.getAuthentication(token);
        log.info("username={}", ((UserDetails) authentication.getPrincipal()).getUsername());
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(testUserEmail);
    }

    @Test
    void getUserId() {
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("userId", userId))
                .build()
                .createToken(issuer, secretKey);
        Long userIdByToken = tokenProvider.getUserId(token);
        assertThat(userIdByToken).isEqualTo(userId);
    }

    @Getter
    static class JwtFactory {
        private String subject = "test@email.com";
        private Date issuedAt = new Date();
        private Date expiration = new Date(new Date().getTime() + Duration.ofMinutes(43200).toMillis());
        private Map<String, Object> claims = emptyMap();

        @Builder
        public JwtFactory(String subject, Date issuedAt, Date expiration, Map<String, Object> claims) {
            this.subject = subject != null ? subject : this.subject;
            this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
            this.expiration = expiration != null ? expiration : this.expiration;
            this.claims = claims != null ? claims : this.claims;
        }

        public static JwtFactory withDefaultValues() {
            return JwtFactory.builder().build();
        }

        public String createToken(String issuer, String secretKey) {
            return Jwts.builder()
                    .setSubject(subject)
                    .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                    .setIssuer(issuer)
                    .setIssuedAt(issuedAt)
                    .setExpiration(expiration)
                    .addClaims(claims)
                    .signWith(SignatureAlgorithm.HS256, secretKey)
                    .compact();
        }
    }
}