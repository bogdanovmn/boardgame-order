package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AdminMenu;
import com.github.bogdanovmn.boardgameorder.web.app.admin.AbstractVisualAdminController;
import com.github.bogdanovmn.boardgameorder.web.etl.PriceListImportService;
import com.github.bogdanovmn.boardgameorder.web.etl.UploadDuplicateException;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ImportType;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Source;
import com.google.common.io.ByteStreams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.HashMap;


@Controller
class PriceListAdminController extends AbstractVisualAdminController {
    private final PriceListImportService priceListImportService;
    private final PriceListChangesService priceListChangesService;

    @Autowired
    PriceListAdminController(PriceListImportService priceListImportService, PriceListChangesService priceListChangesService) {
        this.priceListImportService = priceListImportService;
        this.priceListChangesService = priceListChangesService;
    }

    @Override
    protected AdminMenu.ITEM currentAdminMenuItem() {
        return AdminMenu.ITEM.UPLOAD_PRICE_LIST;
    }

    @PostMapping("/price-list")
    String upload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            Source source = priceListImportService.importFile(
                ByteStreams.toByteArray(file.getInputStream()),
                ImportType.MANUAL
            );
            priceListChangesService.updateAllChanges();
            redirectAttributes.addFlashAttribute("msg", "OK!");
            redirectAttributes.addFlashAttribute("source", source);
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute(
                "customError",
                String.format("Что-то пошло не так при загрузке файла (%s)", e.getMessage())
            );
        } catch (UploadDuplicateException e) {
            redirectAttributes.addFlashAttribute("customError", e.getMessage());
        }

        return "redirect:/admin/price-list";
    }

    @GetMapping("/price-list")
    ModelAndView form(
        @RequestHeader(name = "referer", required = false) String referer) {
        return new ModelAndView(
            "upload_price_list",
            new HashMap<String, Object>() {{
                put("referer", referer);
            }}
        );
    }

    @GetMapping("/price-list-changes-update")
    String changesUpdate() {
        priceListChangesService.updateAllChanges();
        return "redirect:/price-list/changes/last";
    }
}
