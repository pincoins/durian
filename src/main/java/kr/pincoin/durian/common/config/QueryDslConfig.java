package kr.pincoin.durian.common.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {
    @PersistenceContext
    private EntityManager em;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        // Concurrency issue
        // EntityManager DI when creating JPAQueryFactory instance
        // EntityManager instance separately / concurrently works in a transaction unit
        return new JPAQueryFactory(em);
    }
}