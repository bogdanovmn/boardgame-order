package com.github.bogdanovmn.boardgameorder.web.app.config.profiler;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.jta.JtaTransactionManager;

import javax.sql.DataSource;
import java.util.Map;

@Configuration
public class HibernateConfiguration extends HibernateJpaAutoConfiguration {
	@Autowired
	private HibernateStatisticsInterceptor interceptor;

	public HibernateConfiguration(
		DataSource dataSource,
		JpaProperties jpaProperties,
		ObjectProvider<JtaTransactionManager> jtaTransactionManager,
		ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers
	) {
		super(dataSource, jpaProperties, jtaTransactionManager, transactionManagerCustomizers);
	}

	@Bean
	public HibernateStatisticsInterceptor hibernateInterceptor() {
		return new HibernateStatisticsInterceptor();
	}
	@Override
	protected void customizeVendorProperties(Map<String, Object> vendorProperties) {
		vendorProperties.put("hibernate.ejb.interceptor", interceptor);
	}
}