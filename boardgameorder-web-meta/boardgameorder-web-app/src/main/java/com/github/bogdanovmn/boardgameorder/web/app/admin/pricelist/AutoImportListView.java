package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.AutoImport;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.AutoImportRepository.AutoImportHistory;

import java.util.List;

class AutoImportListView {
    private final AutoImport lastImport;
    private final List<AutoImportHistory> history;

    AutoImportListView(AutoImport lastImport, List<AutoImportHistory> history) {
        this.lastImport = lastImport;
        this.history = history;
    }

    List<AutoImportHistory> getHistory() {
        return history;
    }

    AutoImport getLastImport() {
        return lastImport;
    }
}
