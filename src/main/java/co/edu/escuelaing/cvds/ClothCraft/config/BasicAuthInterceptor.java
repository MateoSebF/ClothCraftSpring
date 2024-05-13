package co.edu.escuelaing.cvds.ClothCraft.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import co.edu.escuelaing.cvds.ClothCraft.model.Session;
import co.edu.escuelaing.cvds.ClothCraft.repository.SessionRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/*
 * Class that handles the basic authentication of the users
 * with the methods preHandle, postHandle and afterCompletion
 */
@Component
public class BasicAuthInterceptor implements HandlerInterceptor {

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
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String isStaticParam = request.getParameter("isStatic");
        boolean isStatic = Boolean.parseBoolean(isStaticParam);
        boolean isAllowed = false;
        if (isStatic) {
            isAllowed = true;
        } else {
            String authToken = getCookieValue(request, "cookie");
            if (authToken != null) {
                Session session = sessionRepository.findByToken(UUID.fromString(authToken));
                if (session != null) {
                    String userId = session.getUser().getId();
                    Duration duration = Duration.between(Instant.now(), session.getTimestamp());
                    long oneHour = 60L * 60L;
                    if (duration.getSeconds() > oneHour) {
                        sessionRepository.delete(session);
                        response.sendError(HttpServletResponse.SC_EXPECTATION_FAILED, "SessionTimeout");
                        isAllowed = false;
                    } else {
                        String requestURI = request.getRequestURI();
                        String queryString = request.getQueryString() != null ? request.getQueryString() : "";
                        String userIdParam = "userId=" + userId;
                        String newQueryString = queryString.isEmpty() ? userIdParam : queryString + "&" + userIdParam;
                        newQueryString += "&isStatic=true";
                        String newRequestURI = requestURI + "?" + newQueryString;
                        request.getRequestDispatcher(newRequestURI).forward(request, response);
                        isAllowed = false;
                    }
                } else {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad Request");
                    isAllowed = false;
                }
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                isAllowed = false;
            }
        }
        return isAllowed;
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
