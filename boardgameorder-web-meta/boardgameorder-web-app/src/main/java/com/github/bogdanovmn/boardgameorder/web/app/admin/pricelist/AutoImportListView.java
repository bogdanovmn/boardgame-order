package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.AutoImport;

import java.util.List;

class AutoImportListView {
	private final AutoImport lastImport;
	private final List<AutoImport> history;

	AutoImportListView(AutoImport lastImport, List<AutoImport> history) {
		this.lastImport = lastImport;
		this.history = history;
	}

	List<AutoImport> getHistory() {
		return history;
	}

	AutoImport getLastImport() {
		return lastImport;
	}
}
