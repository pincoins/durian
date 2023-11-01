package kr.pincoin.durian.auth.util.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public static final String ERROR_401_INVALID_SECRET_KEY = "1001";
    public static final String ERROR_401_EXPIRED_JWT = "1002";
    public static final String ERROR_401_INVALID_TOKEN = "1003";
    public static final String ERROR_401_USER_NOT_FOUND = "1004";
    public static final String ERROR_401_UNKNOWN = "1005";

    @Override
    public void
    commence(HttpServletRequest request,
             HttpServletResponse response,
             AuthenticationException authException) throws IOException {
        // 401 Unauthorized
        String exception = (String) (request.getAttribute("exception"));

        try {
            if (exception == null) {
                setResponse(response,
                            ERROR_401_UNKNOWN,
                            authException != null ? authException.getLocalizedMessage() : "security config error");
            } else if (exception.equals(ERROR_401_INVALID_SECRET_KEY)) {
                setResponse(response,
                            ERROR_401_INVALID_SECRET_KEY,
                            "invalid secret key");
            } else if (exception.equals(ERROR_401_EXPIRED_JWT)) {
                setResponse(response,
                            ERROR_401_EXPIRED_JWT,
                            "expired token");
            } else if (exception.equals(ERROR_401_INVALID_TOKEN)) {
                setResponse(response,
                            ERROR_401_INVALID_TOKEN,
                            "invalid token format");
            } else if (exception.equals(ERROR_401_USER_NOT_FOUND)) {
                setResponse(response,
                            ERROR_401_USER_NOT_FOUND,
                            "user not found");
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        // does no send error for custom response
        // response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
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