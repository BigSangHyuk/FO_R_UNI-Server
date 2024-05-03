package bigsanghyuk.four_uni.config.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    public static final String ID = "uuid";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uuid = UUID.randomUUID().toString();
        request.setAttribute(ID, uuid);
        log.info("REQUEST [{}][{}][{}]", uuid, request.getRequestURI(), request.getHeader("Authorization"));
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("RESPONSE [{}][{}][{}]", request.getAttribute(ID), request.getRequestURI(), response.getStatus());
        if (ex != null) {
            log.error("error", ex);
        }
    }
}
