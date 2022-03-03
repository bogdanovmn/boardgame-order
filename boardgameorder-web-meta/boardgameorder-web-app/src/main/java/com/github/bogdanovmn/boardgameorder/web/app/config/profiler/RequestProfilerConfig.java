package com.github.bogdanovmn.boardgameorder.web.app.config.profiler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@RequiredArgsConstructor
public class RequestProfilerConfig extends WebMvcConfigurerAdapter {
    private final RequestStatisticsInterceptor requestStatisticsInterceptor;

    @Bean
    public RequestStatisticsInterceptor requestStatisticsInterceptor() {
        return new RequestStatisticsInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestStatisticsInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns("/webjars/**")
            .excludePathPatterns("/js/**")
            .excludePathPatterns("/img/**")
            .excludePathPatterns("/css/**");
    }
}
