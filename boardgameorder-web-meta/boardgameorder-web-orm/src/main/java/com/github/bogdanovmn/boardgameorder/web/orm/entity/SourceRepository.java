package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SourceRepository extends JpaRepository<Source, Integer> {
	Source findFirstByContentHash(String hash);

	Source findTopByOrderByFileModifyDateDesc();

	List<Source> findAllByOrderByFileModifyDateDesc();
}
