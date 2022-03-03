package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.Item;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPrice;

class OrderItemView {
    private final ItemPrice price;
    private final Integer count;

    OrderItemView(final ItemPrice price, final Integer count) {
        this.price = price;
        this.count = count;
    }

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
