package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/price-list")
class PriceListController extends AbstractVisualController {
	private final PriceListService priceListService;

	PriceListController(PriceListService priceListService) {
		this.priceListService = priceListService;
	}

	@GetMapping
	ModelAndView allPrices() {
		return new ModelAndView("price_list", priceListService.getAllItems());
	}

	@Override
	protected HeadMenu.ITEM currentMenuItem() {
		return HeadMenu.ITEM.PRICE_LIST;
	}
}
