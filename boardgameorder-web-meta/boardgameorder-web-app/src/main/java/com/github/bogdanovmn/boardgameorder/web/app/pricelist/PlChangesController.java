package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

import static com.github.bogdanovmn.boardgameorder.web.app.pricelist.PlChangesFilterToggle.*;

@Controller
@RequestMapping("/price-list/changes")
class PlChangesController extends AbstractVisualController {
	private final PriceListService priceListService;

	@Override
	protected HeadMenu.ITEM currentMenuItem() {
		return HeadMenu.ITEM.PRICE_LIST_CHANGES;
	}

	PlChangesController(PriceListService priceListService) {
		this.priceListService = priceListService;
	}

	@GetMapping("/last")
	ModelAndView last(
		@RequestParam(required = false, defaultValue = "true",  name = "new") Boolean showNew,
		@RequestParam(required = false, defaultValue = "false", name = "count") Boolean showCountChange,
		@RequestParam(required = false, defaultValue = "true",  name = "price") Boolean showPriceChange,
		@RequestParam(required = false, defaultValue = "true",  name = "delete") Boolean showDeleted
	) {
		return new ModelAndView(
			"price_list_changes_by_publisher",
			"priceChanges",
			priceListService.priceListLastChangesView(
				new PlChangesFilter(
					"/price-list/changes/last",
					new HashMap<PlChangesFilterToggle, Boolean>() {{
						put(COUNT, showCountChange);
						put(PRICE, showPriceChange);
						put(DELETE, showDeleted);
						put(NEW, showNew);
					}}
				)
			)
		);
	}
}
