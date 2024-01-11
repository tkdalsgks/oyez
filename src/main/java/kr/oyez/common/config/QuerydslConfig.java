package kr.oyez.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;

@Configuration
@EnableJpaAuditing
public class QuerydslConfig {

	@Bean
	JPAQueryFactory jpaQueryFatory(EntityManager em) {
		return new JPAQueryFactory(em);
	}
}
