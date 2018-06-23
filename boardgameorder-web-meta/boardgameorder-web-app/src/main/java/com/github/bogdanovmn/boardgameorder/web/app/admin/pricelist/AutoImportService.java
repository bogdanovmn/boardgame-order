package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.AutoImportRepository;
import org.springframework.stereotype.Service;

@Service
class AutoImportService {
	private final AutoImportRepository autoImportRepository;

	AutoImportService(final AutoImportRepository autoImportRepository) {
		this.autoImportRepository = autoImportRepository;
	}

	AutoImportListView getAutoImportListView() {
		return new AutoImportListView(
			autoImportRepository.findAllByOrderById()
		);
	}
}