package burgermap.interceptor;

import burgermap.annotation.CheckLogin;
import burgermap.session.SessionConstants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 로그인 체크 인터셉터
 * 회원에게만 허용되는 API 요청에 대해 로그인 여부 확인
 */
@Slf4j
public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            if (handlerMethod.hasMethodAnnotation(CheckLogin.class)) {
                HttpSession session = request.getSession(false);
                if (session == null || session.getAttribute(SessionConstants.loginMember) == null) {
                    log.debug("[{}] request from client that not logged in", request.getRequestURI());
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    return false;
                }
            }
        }
        return true;
    }
}
