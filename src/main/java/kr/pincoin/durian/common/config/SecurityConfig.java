package kr.pincoin.durian.common.config;


import kr.pincoin.durian.auth.util.jwt.JwtAccessDeniedHandler;
import kr.pincoin.durian.auth.util.jwt.JwtAuthenticationEntryPoint;
import kr.pincoin.durian.auth.util.jwt.JwtFilter;
import kr.pincoin.durian.auth.util.password.DjangoPasswordEncoder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableMethodSecurity
@Getter
@Slf4j
public class SecurityConfig {
    @Value("${security-config.content-security-policy}")
    private String contentSecurityPolicy;

    @Value("${security-config.cors.origins}")
    private List<String> corsOrigins;

    @Value("${security-config.cors.headers}")
    private String corsHeaders;

    @Value("${security-config.cors.methods}")
    private String corsMethods;

    @Value("${security-config.cors.allow-credentials}")
    private boolean corsAllowCredentials;

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Deprecated: WebSecurityConfigurerAdapter inheritance
    // Current: @Bean
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Authentication API

        // Form login (disabled)
        http.formLogin(AbstractHttpConfigurer::disable); // spring security 6.1 or above
        // http.formLogin().disable(); // spring security 6.0

        // http.logout()
        // http.rememberMe()

        // HTTP Basic authentication (disabled)
        http.httpBasic(AbstractHttpConfigurer::disable);

        // CSRF
        // http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));
        http.csrf(AbstractHttpConfigurer::disable);

        // CORS
        // corsConfigurationSource
        http.cors(withDefaults());

        // Exception handling
        http.exceptionHandling(config -> {
            config.authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // 401 Unauthorized: login failure
                    .accessDeniedHandler(new JwtAccessDeniedHandler()); // 403 Forbidden: no permission
        });

        // HTTP protocol headers
        http.headers(headers -> {
            headers.defaultsDisabled();

            // Strict-Transport-Security: max-age=31536000 ; includeSubDomains ; preload
            headers.httpStrictTransportSecurity(hstsConfig -> hstsConfig
                    .includeSubDomains(true)
                    .maxAgeInSeconds(31536000)
                    .preload(true));

            //X-XSS-Protection: 1; mode=block
            headers.xssProtection(xssConfig -> xssConfig.headerValue(
                    XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK));

            // Cache-Control: no-cache, no-store, max-age=0, must-revalidate
            // Pragma: no-cache
            // Expires: 0
            headers.cacheControl(cacheControlConfig -> {
            });

            // Content-Security-Policy: default-src 'none'
            headers.contentSecurityPolicy(contentSecurityPolicyConfig ->
                                                  contentSecurityPolicyConfig.policyDirectives(contentSecurityPolicy));

            // X-Content-Type-Options: nosniff
            headers.contentTypeOptions(contentTypeOptionsConfig -> {
            });

            // X-Frame-Options: SAMEORIGIN | DENY
            headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin);
        });

        // Stateless Session
        http.sessionManagement(session -> {
            session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
            // Login attempt throttling works in spite of stateless JWT authentication
            // maximumSessions(1): Prevents a user from logging in multiple times.
            // A second login will cause the first to be invalidated.
            // maxSessionsPreventsLogin(true): The second login will then be rejected
            // session.maximumSessions(1).maxSessionsPreventsLogin(true);
        });

        // Request resource permission mapping
        http.authorizeHttpRequests(auth -> auth
                                           // authorizing API examples
                                           // requestMatchers().hasRole().permitAll()
                                           // requestMatchers().denyAll()
                                           // .requestMatchers("/auth/**").permitAll()
                                           .requestMatchers("/**").permitAll()
                                           // anyRequest().authenticated() - rememberMe login enabled (form login)
                                           .anyRequest().fullyAuthenticated() //rememberMe disabled
                                  );

        // Add JWT token filter
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new DjangoPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // When allowCredentials is true,
        // allowedOrigins cannot contain the special value "*"
        // since that cannot be set on the "Access-Control-Allow-Origin" response header.
        // To allow credentials to a set of origins,
        // list them explicitly or consider using "allowedOriginPatterns" instead.
        configuration.setAllowedOriginPatterns(corsOrigins);
        configuration.addAllowedHeader(corsHeaders);
        configuration.addAllowedMethod(corsMethods);
        configuration.setAllowCredentials(corsAllowCredentials);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}