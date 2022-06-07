package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart/items/{id}")
@RequiredArgsConstructor
class CartItemController extends AbstractController {
    private final CartService cartService;

    @PutMapping
    ResponseEntity<?> addItem(@PathVariable Integer id) {
        cartService.addItem(getUser(), id);
        return ResponseEntity.ok().build();

    }

    @DeleteMapping
    ResponseEntity<?> deleteItem(@PathVariable Integer id) {
        cartService.deleteItem(getUser(), id);
        return ResponseEntity.ok().build();
    }
}
