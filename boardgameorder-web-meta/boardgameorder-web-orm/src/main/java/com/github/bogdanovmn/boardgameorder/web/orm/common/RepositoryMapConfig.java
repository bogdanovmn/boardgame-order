package com.github.bogdanovmn.boardgameorder.web.orm.common;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.Publisher;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.PublisherRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserRole;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserRoleRepository;
import com.github.bogdanovmn.common.spring.jpa.EntityRepositoryMapFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.github.bogdanovmn.common.spring")
class RepositoryMapConfig {
	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private PublisherRepository publisherRepository;

	@Bean
	EntityRepositoryMapFactory entityRepositoryMapFactory() {
		return new EntityRepositoryMapFactory.Builder()
			.map(UserRole.class,  userRoleRepository)
			.map(Publisher.class, publisherRepository)
			.build();
	}
}
