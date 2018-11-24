package com.github.bogdanovmn.boardgameorder.web.orm.common;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.BaseEntityWithUniqueName;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.BaseEntityWithUniqueNameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class EntityFactory {
	private final Map<Class<?>, Map<String, BaseEntityWithUniqueName>> singleEntityCache = new ConcurrentHashMap<>();
	private final Map<Class<?>, Iterable> setEntityCache = new ConcurrentHashMap<>();

	@Autowired
	private EntityRepositoryMapFactory entityRepositoryMapFactory;

	public EntityFactory() {}

	public <T extends BaseEntityWithUniqueName> T getPersistBaseEntityWithUniqueName(T entity) {
		T result;

		Class<? extends BaseEntityWithUniqueName> entityClass = entity.getClass();
		String name = entity.getName();

		if (!this.singleEntityCache.containsKey(entityClass)) {
			this.singleEntityCache.put(entityClass, new HashMap<>());
		}

		if (!this.singleEntityCache.get(entityClass).containsKey(name)) {
			BaseEntityWithUniqueNameRepository repository = entityRepositoryMapFactory.getRepository(entityClass);

			if (repository == null) {
				throw new RuntimeException(
					String.format("No map repository found for %s", entityClass)
				);
			}

			result = (T) repository.findFirstByName(name);

			if (result != null) {
				this.singleEntityCache.get(entityClass).put(name, result);
			}
			else {
				repository.save(entity);
				result = entity;
			}
		}
		else {
			result = (T) this.singleEntityCache.get(entityClass).get(name);
		}

		return result;
	}

	public Iterable getAll(Class<? extends BaseEntityWithUniqueName> entClass) {
		return this.setEntityCache.computeIfAbsent(
			entClass,
			x -> entityRepositoryMapFactory.getRepository(entClass).findAll()
		);
	}
}