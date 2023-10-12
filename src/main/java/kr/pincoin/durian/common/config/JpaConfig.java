package kr.pincoin.durian.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "kr.pincoin.durian",
        // if basePackages not specified, error occurs:
        //   Spring Data JPA - Could not safely identify store assignment for repository candidate interface
        includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*jpa.*"),
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*redis.*"))
public class JpaConfig {
}