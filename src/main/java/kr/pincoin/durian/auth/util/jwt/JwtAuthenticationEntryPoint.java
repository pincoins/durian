package kr.pincoin.durian.auth.util.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public static final String ERROR_401_USER_NOT_FOUND = "1001";
    public static final String ERROR_401_INSUFFICIENT_AUTHENTICATION = "1002";
    public static final String ERROR_401_UNKNOWN = "1003";

    @Override
    public void
    commence(HttpServletRequest request,
             HttpServletResponse response,
             AuthenticationException authException) throws IOException {
        // 401 Unauthorized
        String exception = (String) (request.getAttribute("exception"));

        try {
            if (authException instanceof InsufficientAuthenticationException) {
                setResponse(response,
                            ERROR_401_INSUFFICIENT_AUTHENTICATION,
                            "Insufficient authentication");
            } else if (exception == null) {
                setResponse(response,
                            ERROR_401_UNKNOWN,
                            "Security config error");
            } else if (exception.equals(ERROR_401_USER_NOT_FOUND)) {
                setResponse(response,
                            ERROR_401_USER_NOT_FOUND,
                            "User not found");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // Do not send error for ApiErrorResponse
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void
    setResponse(HttpServletResponse response,
                String errorCode,
                String errorMessage) throws IOException, JSONException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().print(new JSONObject()
                                           .put("message", errorMessage)
                                           .put("code", errorCode));
    }
}