package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.AutoImportRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.AutoImportStatus;
import org.springframework.stereotype.Service;

@Service
class AutoImportService {
	private final AutoImportRepository autoImportRepository;

	AutoImportService(final AutoImportRepository autoImportRepository) {
		this.autoImportRepository = autoImportRepository;
	}

	AutoImportListView getAutoImportListView() {
		return new AutoImportListView(
			autoImportRepository.findTopByOrderByIdDesc(),
			autoImportRepository.findAllByStatusIsNotOrderByIdDesc(AutoImportStatus.NO_CHANGES)
		);
	}
}
