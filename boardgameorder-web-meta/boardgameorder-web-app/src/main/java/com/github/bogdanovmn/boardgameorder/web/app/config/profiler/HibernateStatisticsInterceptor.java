package com.github.bogdanovmn.boardgameorder.web.app.config.profiler;

import org.hibernate.EmptyInterceptor;

public class HibernateStatisticsInterceptor extends EmptyInterceptor {
    private final ThreadLocal<Long> queryCount = new ThreadLocal<>();

    public void startCounter() {
        queryCount.set(0L);
    }

    public Long getQueryCount() {
        return queryCount.get();
    }

    public void clearCounter() {
        queryCount.remove();
    }

    @Override
    public String onPrepareStatement(String sql) {
        Long count = queryCount.get();
        if (count != null) {
            queryCount.set(count + 1);
        }
        //log.info(sql);
        return super.onPrepareStatement(sql);
    }
}
