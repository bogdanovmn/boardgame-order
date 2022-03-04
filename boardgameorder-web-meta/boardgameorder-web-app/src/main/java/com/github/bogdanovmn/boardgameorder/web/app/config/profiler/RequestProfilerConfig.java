package com.github.bogdanovmn.boardgameorder.web.app.config.profiler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class RequestProfilerConfig implements WebMvcConfigurer {
    private final HibernateStatisticsInterceptor hibernateStatisticsInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RequestStatisticsInterceptor(hibernateStatisticsInterceptor))
            .addPathPatterns("/**")
            .excludePathPatterns("/webjars/**")
            .excludePathPatterns("/js/**")
            .excludePathPatterns("/img/**")
            .excludePathPatterns("/css/**");
    }
}
