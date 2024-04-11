package bigsanghyuk.four_uni.config.jwt;

import bigsanghyuk.four_uni.config.RedisUtil;
import bigsanghyuk.four_uni.config.jwt.service.JpaUserDetailsService;
import bigsanghyuk.four_uni.exception.StatusEnum;
import bigsanghyuk.four_uni.exception.jwt.TokenNotFoundException;
import bigsanghyuk.four_uni.user.domain.entity.Authority;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtProvider {

    @Value("${jwt.issuer}")
    private String issuer;
    @Value("${jwt.secret}")
    private String salt;
    private Key secretKey;
    private final JpaUserDetailsService userDetailsService;
    private final int EXP_MINUTES = 30;   // 30 min
    private final RedisUtil redisUtil;

    private final List<String> notFilteredRoutes = List.of("/", "/sign-up", "/sign-in", "/refresh", "/auth/**", "/healthcheck");

    public String createToken(String email, Long userId, List<Authority> roles) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("userId", userId);
        claims.put("roles", roles);
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setIssuer(issuer)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(EXP_MINUTES).toMillis()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // 권한 정보 가져오기
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰에 담겨 있는 유저 Email
    public String getEmail(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }

    // authorization 헤더에서 인증
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }

    // 검증
    public boolean validateToken(String token) {
        try {
            if (!token.substring(0, "BEARER ".length()).equalsIgnoreCase("BEARER ")) {
                return false;
            } else {
                token = token.split(" ")[1].trim();
            }
            if (redisUtil.hasKeyBlackList(token)) { // access token이 블랙리스트에 있으면 검증 실패
                return false;
            }
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SignatureException e) {
            throw new JwtException(StatusEnum.WRONG_TYPE_TOKEN.getCode());
        } catch (MalformedJwtException e) {
            throw new JwtException(StatusEnum.MALFORMED_TOKEN.getCode());
        } catch (ExpiredJwtException e) {
            throw new JwtException(StatusEnum.ACCESS_TOKEN_EXPIRED.getCode());
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private HashMap<String, Object> getParsedTokenHashMap(String token) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("token", token);
        Claims claims = parseClaims(token);
        map.put("claims", claims);
        return map;
    }

    public HashMap<String, Object> parseJwt(HttpServletRequest request, String authorizationHeader) {
        String requestURI = request.getRequestURI();
        validateAuthorizationHeader(requestURI, authorizationHeader);
        String token = extractToken(authorizationHeader);
        return getParsedTokenHashMap(token);
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring("Bearer ".length());
    }

    private void validateAuthorizationHeader(String requestURI, String header) {
        if (notFilteredRoutes.contains(requestURI)) {
            return;
        }
        if (header == null || !header.startsWith("Bearer ")) {
            throw new TokenNotFoundException();
        }
    }

    public Long getExpiration(String accessToken) {
        Date expiration = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody().getExpiration();
        long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    @PostConstruct
    protected void init() {
        secretKey = Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8));
    }
}
