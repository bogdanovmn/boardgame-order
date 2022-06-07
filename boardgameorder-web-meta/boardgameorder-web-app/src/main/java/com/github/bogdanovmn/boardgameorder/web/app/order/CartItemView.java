package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.Item;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPrice;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CartItemView {
    private final ItemPrice price;
    private final Integer count;

    Integer getCount() {
        return count;
    }

    Integer getTotal() {
        return price.getRoundedPrice() * count;
    }

    Item getItem() {
        return price.getItem();
    }

    Integer getItemPrice() {
        return price.getRoundedPrice();
    }
}
