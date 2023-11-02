package kr.pincoin.durian.auth.util.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Slf4j
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void
    handle(HttpServletRequest request,
           HttpServletResponse response,
           AccessDeniedException accessDeniedException) throws IOException {
        // 403 Forbidden

        // Request without Bearer header is forbidden.
        // Due to no header, error message is not shown.
        log.warn(accessDeniedException.getLocalizedMessage(), accessDeniedException);
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }
}
