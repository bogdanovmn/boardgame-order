package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Publisher;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.CartItem;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class PlPublisherView {
    private final Publisher publisher;
    private final List<ItemPrice> prices;
    private final List<CartItem> cartItems;

    PlPublisherView(final Publisher publisher, final List<ItemPrice> prices, final List<CartItem> cartItems) {
        this.publisher = publisher;
        this.prices = prices;
        this.cartItems = cartItems;
    }

    Publisher getPublisher() {
        return publisher;
    }

    List<PlItemView> getPrices() {
        Set<Integer> ordered = cartItems.stream().map(x -> x.getItem().getId()).collect(Collectors.toSet());
        return prices.stream()
            .sorted(
                Comparator.comparing(
                        (ItemPrice x) ->
                            x.getItem().isLikeBoardGameTitle()
                    )
                    .reversed()
                    .thenComparing(
                        (ItemPrice x) ->
                            x.getItem().getEffectiveTitle()
                    )
            )
            .map(x -> new PlItemView(x, ordered.contains(x.getItem().getId())))
            .collect(Collectors.toList());
    }
}
