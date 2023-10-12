package kr.pincoin.durian.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "kr.pincoin.durian",
        // if basePackages not specified, error occurs:
        //   Spring Data Redis - Could not safely identify store assignment for repository candidate interface
        includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*redis.*"),
        excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*jpa.*"))
public class RedisConfig {
}
