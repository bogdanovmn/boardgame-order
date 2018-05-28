package com.github.bogdanovmn.boardgameorder.web.orm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SourceRepository extends JpaRepository<Source, Integer> {
	Source findFirstByContentHash(String hash);
}
