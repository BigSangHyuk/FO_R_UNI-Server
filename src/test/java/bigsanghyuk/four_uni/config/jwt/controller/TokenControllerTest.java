package bigsanghyuk.four_uni.config.jwt.controller;

import bigsanghyuk.four_uni.config.jwt.domain.RefreshToken;
import bigsanghyuk.four_uni.config.jwt.domain.RefreshTokenRepository;
import bigsanghyuk.four_uni.config.jwt.dto.request.CreateAccessTokenRequest;
import bigsanghyuk.four_uni.user.domain.entity.User;
import bigsanghyuk.four_uni.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TokenControllerTest {

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    protected MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    WebApplicationContext context;
    @Autowired
    RefreshTokenRepository refreshTokenRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void createNewAccessToken() throws Exception {
        //given
        final String url = "/jwt/create-access-token";
        User testUser = userRepository.save(User.builder()
                .id(100L)
                .email("jwt@jwt.com")
                .password("jwt")
                .name("jwt")
                .dept(100)
                .nickName("jwt")
                .image("jwt")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build()
        );
        String refreshToken = JwtFactory.builder()
                .claims(Map.of("userId", testUser.getId()))
                .build()
                .createToken(issuer, secretKey);
        refreshTokenRepository.save(new RefreshToken(testUser.getId(), refreshToken));
        CreateAccessTokenRequest request = new CreateAccessTokenRequest();
        request.updateRefreshToken(refreshToken);
        final String requestBody = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        //then
        resultActions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
    }

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        userRepository.deleteAll();
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

        public static TokenControllerTest.JwtFactory withDefaultValues() {
            return TokenControllerTest.JwtFactory.builder().build();
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