package bigsanghyuk.four_uni.config.filter;

import bigsanghyuk.four_uni.exception.StatusEnum;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals("/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            String message = e.getMessage();
            if (StatusEnum.WRONG_TYPE_TOKEN.getCode().equals(message)) {
                responseException(response, StatusEnum.WRONG_TYPE_TOKEN, "잘못된 타입의 토큰입니다.");
            } else if (StatusEnum.MALFORMED_TOKEN.getCode().equals(message)) {
                responseException(response, StatusEnum.MALFORMED_TOKEN, "값이 유효하지 않습니다.");
            } else if (StatusEnum.ACCESS_TOKEN_EXPIRED.getCode().equals(message)) {
                responseException(response, StatusEnum.ACCESS_TOKEN_EXPIRED, "만료된 토큰입니다.");
            }
        }
    }

    private void responseException(HttpServletResponse response, StatusEnum statusEnum, String message) throws RuntimeException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusEnum.getStatusCode());
        JSONObject jsonResponse = new JSONObject();
        jsonResponse.put("status", statusEnum.getCode());
        jsonResponse.put("message", message);
        response.getWriter().print(jsonResponse);
    }
}
