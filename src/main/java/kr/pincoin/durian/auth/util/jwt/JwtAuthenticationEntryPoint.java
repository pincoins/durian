package kr.pincoin.durian.auth.util.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    public static final String ERROR_401_INVALID_TOKEN = "1001";
    public static final String ERROR_401_EXPIRED_TOKEN = "1002";
    public static final String ERROR_401_INSUFFICIENT_AUTHENTICATION = "1003";
    public static final String ERROR_401_USERNAME_NOT_FOUND = "1004";
    public static final String ERROR_401_UNKNOWN = "1005";

    @Override
    public void
    commence(HttpServletRequest request,
             HttpServletResponse response,
             AuthenticationException authException) throws IOException {
        // 401 Unauthorized
        try {
            log.warn("Authentication Entry Point", authException);

            if (authException instanceof BadCredentialsException) {
                setResponse(response,
                            ERROR_401_INVALID_TOKEN,
                            "Invalid access token or invalid secret key");
            } else if (authException instanceof CredentialsExpiredException) {
                setResponse(response,
                            ERROR_401_EXPIRED_TOKEN,
                            "Expired access token");
            } else if (authException instanceof InsufficientAuthenticationException) {
                setResponse(response,
                            ERROR_401_INSUFFICIENT_AUTHENTICATION,
                            "Access token missing");
            } else if (authException instanceof UsernameNotFoundException) {
                setResponse(response,
                            ERROR_401_USERNAME_NOT_FOUND,
                            "Username not found");
            } else {
                setResponse(response,
                            ERROR_401_UNKNOWN,
                            "Authentication failure");
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