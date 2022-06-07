package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.app.SourceService;
import com.github.bogdanovmn.boardgameorder.web.app.Cart;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
class CartService {
    private final CartItemRepository cartItemRepository;
    private final ItemRepository itemRepository;
    private final SourceService sourceService;

    void addItem(final User user, final Integer id) {
        Item item = itemRepository.findById(id).orElseThrow(
            () -> new IllegalStateException("Item not exists: " + id)
        );

        CartItem orderItem = cartItemRepository.findFirstByUserAndItem(user, item);
        if (orderItem == null) {
            cartItemRepository.save(
                new CartItem()
                    .setUser(user)
                    .setItem(item)
                    .setCount(1)
            );
        } else {
            cartItemRepository.save(
                orderItem.incCount(1)
            );
        }
    }

    void deleteItem(final User user, final Integer id) {
        Item item = itemRepository.findById(id).orElseThrow(
            () -> new IllegalStateException("Item not exists: " + id)
        );

        CartItem orderItem = cartItemRepository.findFirstByUserAndItem(user, item);
        if (orderItem == null) {
            throw new IllegalStateException("Item order not exists: " + id);
        } else {
            orderItem.incCount(-1);
            if (orderItem.getCount() == 0) {
                cartItemRepository.delete(orderItem);
            } else {
                cartItemRepository.save(orderItem.setUpdated(new Date()));
            }
        }
    }

    // TODO можно грузить не весь прайс, а только заказанные позиции
    private CartView getUserCartView(User user, List<ItemPrice> prices) {
        return new CartView(
            Cart.fromAllPrices(
                prices,
                cartItemRepository.getAllByUser(user)
            )
        );
    }

    CartView getUserCartViewForCurrentPrice(User user) {
        return getUserCartView(user, sourceService.actualPrices());
    }

    CartView getUserCartViewForSpecificPrice(User user, Integer sourceId) {
        return getUserCartView(user, sourceService.prices(sourceId));
    }
}
