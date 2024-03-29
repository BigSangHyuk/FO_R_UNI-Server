package bigsanghyuk.four_uni.config.interceptor;

import bigsanghyuk.four_uni.config.jwt.JwtProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;

    private static final List<String> PERMIT_URLS = List.of("/", "/sign-up", "/sign-in", "/refresh", "/auth/**");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
        String requestURI = request.getRequestURI();
        if (PERMIT_URLS.contains(requestURI) && request.getHeader(HttpHeaders.AUTHORIZATION) == null) {
            request.setAttribute("userId", null);
            return true;
        }
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        setRequestAttributeUserId(request, authorizationHeader);
        return true;
    }

    private void setRequestAttributeUserId(HttpServletRequest request, String authorizationHeader) {
        HashMap<String, Object> parseJwtMap = jwtProvider.parseJwt(request, authorizationHeader);
        Long userId = getUserIdOfToken(parseJwtMap);
        request.setAttribute("userId", userId);
    }

    private Long getUserIdOfToken(HashMap<String, Object> parseJwtMap) {
        Claims claims = (Claims) parseJwtMap.get("claims");
        Integer integerUserId = (Integer) claims.get("userId");
        return Long.valueOf(integerUserId);
    }
}
