package kr.pincoin.durian.auth.util.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class HttpAccessDeniedHandler implements AccessDeniedHandler {
    public static final String ERROR_403_FORBIDDEN = "3001";

    @Override
    public void
    handle(HttpServletRequest request,
           HttpServletResponse response,
           AccessDeniedException ex) throws IOException {
        // 403 Forbidden

        // Request without Bearer header is forbidden.
        // Due to no header, error message is not shown.
        log.warn(ex.getLocalizedMessage(), ex);
        try {
            setResponse(response, ERROR_403_FORBIDDEN, ex.getMessage());
            // response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void
    setResponse(HttpServletResponse response,
                String errorCode,
                String errorMessage) throws IOException, JSONException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().print(new JSONObject()
                                           .put("message", errorMessage)
                                           .put("code", errorCode));
    }
}
