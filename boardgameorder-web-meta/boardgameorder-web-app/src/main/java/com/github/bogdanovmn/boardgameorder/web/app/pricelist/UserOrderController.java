package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import com.github.bogdanovmn.boardgameorder.web.orm.UserOrderItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/user/order")
class UserOrderController extends AbstractVisualController {
	private final UserOrderService userOrderService;

	UserOrderController(final UserOrderService userOrderService) {
		this.userOrderService = userOrderService;
	}

	@GetMapping("/items")
	ModelAndView getItems(Integer id) {
		List<UserOrderItem> items = userOrderService.getAllItems(getUser());
		return new ModelAndView(
			"user_order_items",
			new HashMap<String, Object>() {{
				put("items", items);
			}}
		);
	}

	@Override
	protected HeadMenu.ITEM currentMenuItem() {
		return HeadMenu.ITEM.ORDER;
	}
}