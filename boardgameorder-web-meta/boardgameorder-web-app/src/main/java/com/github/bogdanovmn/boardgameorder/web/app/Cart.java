package com.github.bogdanovmn.boardgameorder.web.app;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.CartItem;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Cart {
    private final Map<Integer, ItemPrice> itemPrices;
    private final Map<Integer, Integer> orderedItems;

    public static Cart fromAllPrices(final List<ItemPrice> itemPrices, final List<CartItem> userOrderItems) {
        Map<Integer, Integer> orderedItems = userOrderItems.stream()
            .collect(
                Collectors.toMap(x -> x.getItem().getId(), CartItem::getCount)
            );

        Map<Integer, ItemPrice> prices = itemPrices.stream()
            .filter(x -> orderedItems.containsKey(x.getItem().getId()))
            .collect(Collectors.toMap(
                x -> x.getItem().getId(), x -> x
            ));

        for (Integer itemId : new ArrayList<>(orderedItems.keySet())) {
            if (!prices.containsKey(itemId)) {
                orderedItems.remove(itemId);
            }
        }

        return new Cart(prices, orderedItems);
    }

    public Integer getItemsCount() {
        return getTotal() > 0
            ? orderedItems.values().stream()
            .mapToInt(x -> x)
            .sum()
            : 0;
    }

    public Integer getTotal() {
        return itemPrices.values().stream()
            .mapToInt(x -> x.getRoundedPrice() * orderedItems.get(x.getItem().getId()))
            .sum();
    }

    public Integer getTotalWithFixPrice() {
        return itemPrices.values().stream()
            .filter(x -> x.getItem().isLikeFixPriceTitle())
            .mapToInt(x -> x.getRoundedPrice() * orderedItems.get(x.getItem().getId()))
            .sum();
    }

    public Set<Integer> items() {
        return orderedItems.keySet();
    }

    public ItemPrice itemPrice(final Integer itemId) {
        return itemPrices.get(itemId);
    }

    public Integer itemCount(final Integer itemId) {
        return orderedItems.get(itemId);
    }
}
