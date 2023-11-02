package kr.pincoin.durian.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.pincoin.durian.auth.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaRepositories(basePackages = "kr.pincoin.durian",
        // if basePackages not specified, error occurs:
        //   Spring Data JPA - Could not safely identify store assignment for repository candidate interface
        includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*jpa.*"),
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*redis.*"))
@EnableRedisRepositories(basePackages = "kr.pincoin.durian",
        // if basePackages not specified, error occurs:
        //   Spring Data Redis - Could not safely identify store assignment for repository candidate interface
        includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*redis.*"),
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*jpa.*"))
@EnableJpaAuditing // created, modified fields = Django compatible
@RequiredArgsConstructor
@Slf4j
public class PersistenceConfig {
    @PersistenceContext
    private final EntityManager em;

    // DataSource bean injection by Spring boot 'Auto configuration' with `application.yaml`

    // JdbcTemplate bean injection by `spring-boot-starter-jdbc` (DataSource injected)
    // Instances of the JdbcTemplate class are thread-safe, once configured.
    // This is important because it means that you can configure a single instance of a JdbcTemplate
    // and then safely inject this shared reference into multiple DAOs (or repositories).
    // The JdbcTemplate is stateful, in that it maintains a reference to a DataSource,
    // but this state is not "conversational state (stateless session)".

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        // QueryDSL configuration
        // Concurrency issue
        // EntityManager DI when creating JPAQueryFactory instance
        // EntityManager instance separately / concurrently works in a transaction unit
        return new JPAQueryFactory(em);
    }

    @Bean
    AuditorAware<Long> auditorProvider() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
                // if authentication instanceof AnonymousAuthenticationToken:
                //  authentication.isAuthenticated() == true
                return Optional.empty();
            }

            return Optional.of(((User) authentication.getPrincipal()).getId());
        };
    }
}
