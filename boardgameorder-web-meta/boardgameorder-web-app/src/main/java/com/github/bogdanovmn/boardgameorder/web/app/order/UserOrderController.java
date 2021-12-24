package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import com.github.bogdanovmn.common.spring.mvc.ViewTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	ModelAndView getItems(
		@RequestParam(required = false, name = "source_id") Integer sourceId
	) {
		return new ViewTemplate("user_order_items")
			.with(
				"order",
				userOrderService.getUserOrderView(getUser(), sourceId)
			)
			.modelAndView();
	}

	@Override
	protected HeadMenu.ITEM currentMenuItem() {
		return HeadMenu.ITEM.ORDER;
	}
}