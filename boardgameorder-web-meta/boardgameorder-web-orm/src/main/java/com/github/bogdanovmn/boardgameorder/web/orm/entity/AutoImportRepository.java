package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoImportRepository extends JpaRepository<AutoImport, Integer> {
	AutoImport findTopByStatusOrderByIdDesc(AutoImportStatus status);

	AutoImport findTopByOrderByIdDesc();

	List<AutoImport> findAllByStatusIsNotOrderByIdDesc(AutoImportStatus status);
}
