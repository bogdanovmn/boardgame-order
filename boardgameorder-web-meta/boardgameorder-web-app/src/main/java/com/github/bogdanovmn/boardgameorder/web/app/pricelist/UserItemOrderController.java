package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/order")
class UserItemOrderController extends AbstractController {
	private final UserOrderService userOrderService;

	UserItemOrderController(final UserOrderService userOrderService) {
		this.userOrderService = userOrderService;
	}

	@PutMapping("/items/{id}")
	ResponseEntity addItem(@PathVariable Integer id) {
		userOrderService.addItem(getUser(), id);
		return ResponseEntity.ok().build();

	}

	@DeleteMapping("/items/{id}")
	ResponseEntity deleteItem(Integer id) {
		userOrderService.deleteItem(getUser(), id);
		return ResponseEntity.ok().build();
	}
}