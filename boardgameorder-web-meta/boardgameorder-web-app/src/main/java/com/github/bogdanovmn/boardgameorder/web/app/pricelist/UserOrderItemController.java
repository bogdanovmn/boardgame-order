package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order/items/{id}")
class UserOrderItemController extends AbstractController {
	private final UserOrderService userOrderService;

	UserOrderItemController(final UserOrderService userOrderService) {
		this.userOrderService = userOrderService;
	}

	@PutMapping
	ResponseEntity addItem(@PathVariable Integer id) {
		userOrderService.addItem(getUser(), id);
		return ResponseEntity.ok().build();

	}

	@DeleteMapping
	ResponseEntity deleteItem(@PathVariable Integer id) {
		userOrderService.deleteItem(getUser(), id);
		return ResponseEntity.ok().build();
	}
}