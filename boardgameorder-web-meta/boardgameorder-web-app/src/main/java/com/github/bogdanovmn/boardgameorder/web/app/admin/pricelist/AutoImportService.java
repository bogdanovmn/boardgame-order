package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.AutoImportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class AutoImportService {
    private final AutoImportRepository autoImportRepository;

    AutoImportListView getAutoImportListView() {
        return new AutoImportListView(
            autoImportRepository.findTopByOrderByIdDesc().orElse(null),
            autoImportRepository.getLastHistory()
        );
    }
}
