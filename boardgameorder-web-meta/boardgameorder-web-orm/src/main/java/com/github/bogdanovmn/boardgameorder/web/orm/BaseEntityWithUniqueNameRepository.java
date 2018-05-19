package com.github.bogdanovmn.boardgameorder.web.orm;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseEntityWithUniqueNameRepository<EntityT extends BaseEntityWithUniqueName>
	extends JpaRepository<EntityT, Integer>
{
	EntityT findFirstByName(String name);
}