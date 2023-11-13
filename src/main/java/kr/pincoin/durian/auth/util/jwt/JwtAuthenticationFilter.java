package kr.pincoin.durian.auth.util.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import static kr.pincoin.durian.auth.util.jwt.JwtAuthenticationEntryPoint.ERROR_401_USER_NOT_FOUND;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // OncePerRequestFilter
    // GenericFilterBean

    private final TokenProvider tokenProvider;

    private final UserDetailsService userDetailsService;

    @Override
    protected void
    doFilterInternal(@NonNull HttpServletRequest request,
                     @NonNull HttpServletResponse response,
                     FilterChain chain) throws ServletException, IOException {
        Optional.ofNullable(tokenProvider.getBearerToken(request))
                // 1. Retrieve access token from headers
                .flatMap(token -> tokenProvider.validateAccessToken(token, request))
                // 2. Retrieve user after jwt validation
                .ifPresent(sub -> {
                    UserDetails userDetails;

                    try {
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
                    } catch (UsernameNotFoundException ignored) {
                        request.setAttribute("exception", ERROR_401_USER_NOT_FOUND);
                        log.warn("{} is not found.", sub);
                    }
                });

        // 8. Execute filter chain
        chain.doFilter(request, response);
    }
}
