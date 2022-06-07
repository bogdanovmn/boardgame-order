package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    CartItem findFirstByUserAndItem(User user, Item item);

    List<CartItem> getAllByUser(User user);

    void deleteAllByUser(User user);
}
