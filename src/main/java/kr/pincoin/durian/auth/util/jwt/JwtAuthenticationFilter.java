package kr.pincoin.durian.auth.util.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter
    // GenericFilterBean

    private final TokenProvider tokenProvider;

    private final UserDetailsService userDetailsService;

    public static final String ERROR_401_USER_NOT_FOUND = "2001";

    public static final String ERROR_401_INVALID_SECRET_KEY = "2002";

    public static final String ERROR_401_EXPIRED_JWT = "2003";

    public static final String ERROR_401_INVALID_TOKEN = "2004";

    @Override
    protected void
    doFilterInternal(@NonNull HttpServletRequest request,
                     @NonNull HttpServletResponse response,
                     FilterChain chain) throws ServletException, IOException {
        try {
            Optional.ofNullable(tokenProvider.getBearerToken(request))
                    // 1. Retrieve access token from headers
                    //  - throws JwtException
                    //  - ERROR_401_INVALID_SECRET_KEY, ERROR_401_EXPIRED_JWT, ERROR_401_INVALID_TOKEN)
                    .flatMap(tokenProvider::validateAccessToken)
                    // 2. Retrieve user after jwt validation
                    .ifPresent(sub -> {
                        UserDetails userDetails;
                            // 3. Lookup user in databases
                            userDetails = userDetailsService.loadUserByUsername(sub);

                            // 4. Authenticate user in context
                            // AuthenticationManager or AuthenticationProvider implementation: isAuthenticated() = true

                            // 5. Create authentication instance
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(
                                            userDetails, // principal: userDetails
                                            null, // credentials
                                            userDetails.getAuthorities() // roles
                                    );

                            // 6. Save WebAuthenticationDetails, WebAuthenticationDetailsSource
                            // authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            // 7. Save user in context
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                    });

            // 8. Execute filter chain
            chain.doFilter(request, response);
        } catch (JwtException ex) {
            setResponse(response, ex.getCode(), ex.getMessage());
            // response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (UsernameNotFoundException ignored) {
            setResponse(response, ERROR_401_USER_NOT_FOUND, "User not found");
            // response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void
    setResponse(HttpServletResponse response,
                String errorCode,
                String errorMessage) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        try {
            response.getWriter().print(new JSONObject()
                                               .put("message", errorMessage)
                                               .put("code", errorCode));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
