package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/price-list/changes")
class PriceListChangesController extends AbstractVisualController {
	private final PriceListService priceListService;

	@Override
	protected HeadMenu.ITEM currentMenuItem() {
		return HeadMenu.ITEM.PRICE_LIST_CHANGES;
	}

	PriceListChangesController(PriceListService priceListService) {
		this.priceListService = priceListService;
	}

	@GetMapping("/last")
	ModelAndView last() {
		return new ModelAndView(
			"price_list_changes_by_publisher",
			"priceChanges",
			priceListService.priceListLastChangesView().getLastChanges()
		);
	}
}
