package com.github.bogdanovmn.boardgameorder.web.app.config.profiler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class RequestStatisticsInterceptor implements AsyncHandlerInterceptor {
    private final ThreadLocal<Long> time = new ThreadLocal<>();
    @Autowired
    private HibernateStatisticsInterceptor statisticsInterceptor;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        time.set(System.currentTimeMillis());
        statisticsInterceptor.startCounter();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        long duration = System.currentTimeMillis() - time.get();
        Long queryCount = statisticsInterceptor.getQueryCount();
        statisticsInterceptor.clearCounter();
        time.remove();
        LOG.info("[Time: {} ms] [SQL: {}] {} {}", duration, queryCount, request.getMethod(), request.getRequestURI());
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //concurrent handling cannot be supported here
        statisticsInterceptor.clearCounter();
        time.remove();
    }
}
