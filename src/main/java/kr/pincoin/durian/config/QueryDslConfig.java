package kr.pincoin.durian.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        // Concurrency issue
        // EntityManager DI when creating JPAQueryFactory instance
        // EntityManager instance separately / concurrently works in a transaction unit
        return new JPAQueryFactory(entityManager);
    }
}