package com.github.bogdanovmn.boardgameorder.web.orm.common;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class EntityRepositoryMapFactory {
	private Map<Class<? extends BaseEntityWithUniqueName>, BaseEntityWithUniqueNameRepository> map;

	@Autowired
	private UserRoleRepository userRoleRepository;
	@Autowired
	private PublisherRepository publisherRepository;

	public EntityRepositoryMapFactory() {
	}

	@PostConstruct
	private void init() {
		this.map = new HashMap<Class<? extends BaseEntityWithUniqueName>, BaseEntityWithUniqueNameRepository>()
		{{
			put(UserRole.class,  userRoleRepository);
			put(Publisher.class, publisherRepository);
		}};
	}

	public BaseEntityWithUniqueNameRepository getRepository(Class<? extends BaseEntityWithUniqueName> aClass) {
		return this.map.get(aClass);
	}
}
