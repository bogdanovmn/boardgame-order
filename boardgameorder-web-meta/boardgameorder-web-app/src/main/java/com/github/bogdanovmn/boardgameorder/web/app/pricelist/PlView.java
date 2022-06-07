package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.Cart;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.CartItem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class PlView {
    private final List<ItemPrice> itemPrices;
    private final List<CartItem> userOrderItems;

    PlView(final List<ItemPrice> itemPrices, final List<CartItem> cartItems) {
        this.itemPrices = itemPrices;
        this.userOrderItems = cartItems;
    }

    PlView(List<ItemPrice> itemPrices) {
        this(itemPrices, new ArrayList<>(0));
    }

    List<PlPublisherView> getPublisherPrices() {
        return itemPrices.stream()
            .collect(
                Collectors.groupingBy(
                    ip -> ip.getItem().getPublisher(),
                    Collectors.toList()
                )
            ).entrySet().stream()
            .map(
                x -> new PlPublisherView(x.getKey(), x.getValue(), userOrderItems)
            )
            .sorted(
                Comparator.comparing(
                    (PlPublisherView x) -> x.getPrices().size()
                ).reversed()
            )
            .collect(Collectors.toList());
    }

    Cart getUserOrder() {
        return Cart.fromAllPrices(itemPrices, userOrderItems);
    }
}
