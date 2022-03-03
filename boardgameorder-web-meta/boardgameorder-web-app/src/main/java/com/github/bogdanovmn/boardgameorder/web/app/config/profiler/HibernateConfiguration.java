package com.github.bogdanovmn.boardgameorder.web.app.config.profiler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibernateConfiguration {

    @Bean
    HibernateStatisticsInterceptor hibernateStatisticsInterceptor() {
        return new HibernateStatisticsInterceptor();
    }

    @Bean
    @Autowired
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(HibernateStatisticsInterceptor interceptor) {
        return hibernateProperties ->
            hibernateProperties.put(
                "hibernate.session_factory.interceptor",
                interceptor
            );
    }
}
