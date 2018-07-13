package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/price-list")
class PlController extends AbstractVisualController {
	private final PlService priceListService;

	PlController(PlService priceListService) {
		this.priceListService = priceListService;
	}

	@GetMapping
	ModelAndView allPrices() {
		return new ModelAndView("price_list", "actualPriceList", priceListService.actualPriceListView(getUser()));
	}

	@Override
	protected HeadMenu.ITEM currentMenuItem() {
		return HeadMenu.ITEM.PRICE_LIST;
	}
}
