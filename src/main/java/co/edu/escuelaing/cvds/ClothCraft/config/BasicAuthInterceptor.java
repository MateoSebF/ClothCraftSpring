package co.edu.escuelaing.cvds.ClothCraft.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import co.edu.escuelaing.cvds.ClothCraft.model.Session;
import co.edu.escuelaing.cvds.ClothCraft.repository.SessionRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.UUID;


@Slf4j
@Component
public class BasicAuthInterceptor implements HandlerInterceptor {

    private final SessionRepository sessionRepository;

    public BasicAuthInterceptor(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }


    private String getCookieValue(HttpServletRequest req, String cookieName) {
        return Arrays.stream(req.getCookies())
                .filter(c -> c.getName().equals(cookieName))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("BasicAuthInterceptor::preHandle()");
        String path = request.getRequestURI();
        log.info("Path:" + path);
        String authToken = getCookieValue(request, "authToken");
        log.info("AuthToken: " + authToken);
        if (authToken != null) {
            Session session = sessionRepository.findByToken(UUID.fromString(authToken));
            log.info("Session: " + session);
            if (session != null) {
                Duration duration = Duration.between(Instant.now(), session.getTimestamp());
                long oneHour = 60L * 60L;
                if(duration.getSeconds() > oneHour) {
                    sessionRepository.delete(session);
                    response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "SessionTimeout");
                    return false;
                } 
                /*else if (path.startsWith("/login/protected") && !session.getUser().getUserRoles().contains(UserRole.ADMINISTRADOR)) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                    return false;
                }*/ 
                else {
                    return true;
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
                return false;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("BasicAuthInterceptor::postHandle()");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        log.info("BasicAuthInterceptor::afterCompletion()");
    }
}
