package co.edu.escuelaing.cvds.ClothCraft.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import static org.apache.commons.text.StringEscapeUtils.escapeHtml4;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import co.edu.escuelaing.cvds.ClothCraft.model.Session;
import co.edu.escuelaing.cvds.ClothCraft.repository.SessionRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/*
 * Class that handles the basic authentication of the users
 * with the methods preHandle, postHandle and afterCompletion
 */
@Slf4j
@Component
public class BasicAuthInterceptor implements HandlerInterceptor {

    private static final Set<String> EXCLUDE_URLS = new HashSet<>();
    static {
        EXCLUDE_URLS.add("/login");
        EXCLUDE_URLS.add("/login/logout");
        EXCLUDE_URLS.add("/user/create");
        EXCLUDE_URLS.add("/user/all");
        EXCLUDE_URLS.add("/wardrobe/all");
        EXCLUDE_URLS.add("/calendary/all");
        EXCLUDE_URLS.add("/clothing/ClothingsTypes");
        EXCLUDE_URLS.add("/outfit/categories");
        // Agrega más URLs que no requieren autenticación
    }

    @Autowired
    private SessionRepository sessionRepository;

    /*
     * Method that gets the cookie value from the request
     * 
     * @param req, the request to get the cookie value from
     * 
     * @param cookieName, the name of the cookie to get the value from
     * 
     * @return String, the value of the cookie
     */
    private String getCookieValue(HttpServletRequest req, String cookieName) {
        String cookieValue = req.getHeader(cookieName);
        String sinAuthToken = null;
        if (cookieValue != null)
            sinAuthToken = cookieValue.replace("authToken=", "");
        return sinAuthToken;
    }
    private boolean isExcludedUri(String requestURI) {
        return EXCLUDE_URLS.contains(requestURI);
    }

    /*
     * Method that handles the preHandle of the request
     * 
     * @param request, the request to handle
     * 
     * @param response, the response to handle
     * 
     * @param handler, the handler to handle
     * 
     * @return boolean, if the request is allowed or not
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        log.info("RequestURI: " + requestURI);
        if (isExcludedUri(requestURI)) {
            return true;
        }
        String authToken = getCookieValue(request, "cookie");
        if (authToken != null) {
            try {
                UUID authTokenUUID = UUID.fromString(authToken);
                Session session = sessionRepository.findByToken(authTokenUUID);
                if (session != null) {
                    Duration duration = Duration.between(session.getTimestamp(), Instant.now());
                    long oneHour = 60L * 60L;
                    if (duration.getSeconds() > oneHour) {
                        sessionRepository.delete(session);
                        response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "SessionTimeout");
                        return false;
                    } else {
                        String userId = escapeHtml4(session.getUser().getId());
                        request.setAttribute("userId", userId);
                        request.setAttribute("sessionValid", true);
                        return true;
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
                    return false;
                }
            } catch (IllegalArgumentException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Token Format");
                return false;
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return false;
        }
    }

    /*
     * Method that handles the postHandle of the request
     * 
     * @param request, the request to handle
     * 
     * @param response, the response to handle
     * 
     * @param handler, the handler to handle
     * 
     * @param modelAndView, the model and view to handle
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    /*
     * Method that handles the afterCompletion of the request
     * 
     * @param request, the request to handle
     * 
     * @param response, the response to handle
     * 
     * @param handler, the handler to handle
     * 
     * @param ex, the exception to handle
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
    }
}
