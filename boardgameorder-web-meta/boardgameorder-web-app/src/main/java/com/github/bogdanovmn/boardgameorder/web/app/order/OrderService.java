package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.CartItemRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Order;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.OrderItem;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.OrderItemRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.OrderRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
class OrderService {
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public void create(User user) {
        CartView cart = cartService.getUserCartViewForCurrentPrice(user);

        Order order = orderRepository.save(
            new Order()
                .setUser(user)
                .setEstimatedCost(
                    cart.estimatedCost()
                )
        );
        orderItemRepository.saveAll(
            cart.getItems().stream().map(
                item -> new OrderItem()
                    .setName(item.getItem().getTitle())
                    .setPrice(item.getItemPrice())
                    .setCount(item.getCount())
                    .setOrder(order)
            ).collect(Collectors.toList())
        );
        cartItemRepository.deleteAllByUser(user);
    }

    @Transactional(readOnly = true)
    public List<Order> last(User user) {
        return orderRepository.findTop10ByUserOrderByIdDesc(user);
    }
}
