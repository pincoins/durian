package kr.pincoin.durian.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
// if basePackages not specified, error occurs:
//   Spring Data Redis - Could not safely identify store assignment for repository candidate interface
@EnableRedisRepositories(basePackages = "kr.pincoin.durian.auth.repository.redis")
public class RedisConfig {
}
