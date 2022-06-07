package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.app.Cart;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Source;
import lombok.RequiredArgsConstructor;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class CartView {
    private final Cart cart;

    Integer getItemsCount() {
        return cart.getItemsCount();
    }

    Integer getTotal() {
        return cart.getTotal();
    }

    Integer getTotalWithFixPrice() {
        return cart.getTotalWithFixPrice();
    }

    List<CartItemView> getItems() {
        return cart.items().stream()
            .sorted(
                Comparator.comparing(itemId ->
                    cart.itemPrice(itemId).getItem().getEffectiveTitle()
                )
            )
            .map(
                itemId -> new CartItemView(
                    cart.itemPrice(itemId),
                    cart.itemCount(itemId)
                )
            )
            .collect(Collectors.toList());
    }

    int estimatedCost() {
        int result = (int) Math.round(
            (cart.getTotal() - cart.getTotalWithFixPrice()) * 0.92 + cart.getTotalWithFixPrice()
        );
        // round to 50 RUB
        int y = result % 50;
        if (y > 0) {
            result = result + 50 - y;
        }
        return result;
    }
}
