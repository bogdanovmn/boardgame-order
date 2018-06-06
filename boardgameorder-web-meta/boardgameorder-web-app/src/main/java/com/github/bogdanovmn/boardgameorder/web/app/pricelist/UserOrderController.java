package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/user/order")
class UserOrderController extends AbstractVisualController {
	private final UserOrderService userOrderService;

	UserOrderController(final UserOrderService userOrderService) {
		this.userOrderService = userOrderService;
	}

	@GetMapping("/items")
	ModelAndView getItems(Integer id) {
		return new ModelAndView(
			"user_order_items",
			"order",
			userOrderService.getUserOrderView(getUser())
		);
	}

	@Override
	protected HeadMenu.ITEM currentMenuItem() {
		return HeadMenu.ITEM.ORDER;
	}
}