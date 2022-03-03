package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/price-lists")
class PlHistoryController extends AbstractVisualController {
    private final PlService priceListService;

    @Override
    protected HeadMenu.ITEM currentMenuItem() {
        return HeadMenu.ITEM.PRICE_LIST_HISTORY;
    }

    PlHistoryController(PlService priceListService) {
        this.priceListService = priceListService;
    }

    @GetMapping
    ModelAndView list() {
        return new ModelAndView(
            "price_lists_history",
            "history",
            priceListService.history()
        );
    }
}
