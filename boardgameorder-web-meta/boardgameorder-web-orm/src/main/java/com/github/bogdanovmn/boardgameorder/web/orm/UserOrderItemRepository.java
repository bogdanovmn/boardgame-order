package com.github.bogdanovmn.boardgameorder.web.orm;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderItemRepository extends JpaRepository<UserOrderItem, Integer> {
	UserOrderItem findFirstByUserAndItem(User user, Item item);
}