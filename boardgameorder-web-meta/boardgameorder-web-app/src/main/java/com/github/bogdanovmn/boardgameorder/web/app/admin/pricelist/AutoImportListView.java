package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.AutoImport;

import java.util.List;

class AutoImportListView {
	private final List<AutoImport> history;

	AutoImportListView(final List<AutoImport> history) {
		this.history = history;
	}

	List<AutoImport> getHistory() {
		return history;
	}
}
