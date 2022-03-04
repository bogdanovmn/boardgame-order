package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AdminMenu;
import com.github.bogdanovmn.boardgameorder.web.app.admin.AbstractVisualAdminController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequiredArgsConstructor
class AutoImportAdminController extends AbstractVisualAdminController {
    private final AutoImportService autoImportService;

    @Override
    protected AdminMenu.ITEM currentAdminMenuItem() {
        return AdminMenu.ITEM.AUTO_IMPORT;
    }

    @GetMapping("/auto-imports")
    ModelAndView autoImports() {
        return new ModelAndView(
            "auto_import_list",
            "autoImport",
            autoImportService.getAutoImportListView()
        );
    }
}
