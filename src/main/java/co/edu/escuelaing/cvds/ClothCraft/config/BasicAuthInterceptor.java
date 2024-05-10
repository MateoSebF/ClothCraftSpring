package co.edu.escuelaing.cvds.ClothCraft.config;

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
import java.util.UUID;

@SuppressWarnings("null")
@Slf4j
@Component
public class BasicAuthInterceptor implements HandlerInterceptor {

    private final SessionRepository sessionRepository;

    public BasicAuthInterceptor(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    private String getCookieValue(HttpServletRequest req, String cookieName) {
        String cookieValue = req.getHeader(cookieName);
        String sinAuthToken = null;
        if(cookieValue != null) {
            sinAuthToken = cookieValue.replace("authToken=", "");
        }
        else log.error("CookieValue is null");

        return sinAuthToken;
    }

    @SuppressWarnings("unused")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        log.info("BasicAuthInterceptor::preHandle()");
        log.info(request.getMethod());
        String path = request.getRequestURI();
        log.info("Path:" + path);
        if ("OPTIONS".equals(request.getMethod())) {
            
            return true;
        }
        String isStaticParam = request.getParameter("isStatic");
        boolean isStatic = Boolean.parseBoolean(isStaticParam);
        log.info("IsStatic: " + isStatic);
        if (isStatic) {
            return true;
        }
        String authToken = getCookieValue(request, "cookie");
        log.info("AuthToken: " + authToken);
        if (authToken != null) {
            Session session = sessionRepository.findByToken(UUID.fromString(authToken));
            if (session != null) {
                log.info("Session: " + session.getToken() + " " + session.getUser().getEmail());
                String userId = session.getUser().getId();
                Duration duration = Duration.between(Instant.now(), session.getTimestamp());
                long oneHour = 60L * 60L;
                if (duration.getSeconds() > oneHour) {
                    sessionRepository.delete(session);
                    response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "SessionTimeout");
                    return false;
                } else {

                    String requestURI = request.getRequestURI();
                    String queryString = request.getQueryString() != null ? request.getQueryString() : "";
                    String userIdParam = "userId=" + userId;
                    String newQueryString = queryString.isEmpty() ? userIdParam : queryString + "&" + userIdParam;

                    // Agrega el parámetro isStatic=true al final de la cadena de consulta
                    newQueryString += "&isStatic=true";

                    String newRequestURI = requestURI + "?" + newQueryString;
                    System.out.println("New Request URI: " + newRequestURI);
                    // Actualizamos la URL de la solicitud
                    request.getRequestDispatcher(newRequestURI).forward(request, response);
                    return false;
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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        log.info("BasicAuthInterceptor::postHandle()");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        log.info("BasicAuthInterceptor::afterCompletion()");
    }
}
