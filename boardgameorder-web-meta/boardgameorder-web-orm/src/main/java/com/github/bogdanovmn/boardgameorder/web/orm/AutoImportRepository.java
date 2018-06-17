package com.github.bogdanovmn.boardgameorder.web.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoImportRepository extends JpaRepository<AutoImport, Integer> {
	AutoImport findTopByStatusOrderByFileModifyDateDesc(AutoImportStatus status);

	List<AutoImport> findAllByOrderById();
}
