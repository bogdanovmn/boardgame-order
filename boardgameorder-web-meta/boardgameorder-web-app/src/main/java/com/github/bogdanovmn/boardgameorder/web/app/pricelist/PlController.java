package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import com.github.bogdanovmn.common.spring.mvc.ViewTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/price-lists")
class PlController extends AbstractVisualController {
	private final PlService priceListService;

	PlController(PlService priceListService) {
		this.priceListService = priceListService;
	}

	@GetMapping("/last")
	ModelAndView allPrices() {
		return new ModelAndView("price_list", "actualPriceList", priceListService.actualPriceListView(getUser()));
	}

	@GetMapping("/{id}")
	ModelAndView allPrices(@PathVariable Integer id) {
		return new ViewTemplate("price_list")
			.with("actualPriceList", priceListService.priceListView(id))
			.modelAndView();
	}

	@Override
	protected HeadMenu.ITEM currentMenuItem() {
		return HeadMenu.ITEM.PRICE_LIST;
	}
}
