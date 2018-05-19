package com.github.bogdanovmn.boardgameorder.web.app.config.profiler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class RequestProfilerConfig extends WebMvcConfigurerAdapter {

	@Autowired
	RequestStatisticsInterceptor requestStatisticsInterceptor;

	@Bean
	public RequestStatisticsInterceptor requestStatisticsInterceptor() {
		return new RequestStatisticsInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(requestStatisticsInterceptor).addPathPatterns("/**");
	}
}