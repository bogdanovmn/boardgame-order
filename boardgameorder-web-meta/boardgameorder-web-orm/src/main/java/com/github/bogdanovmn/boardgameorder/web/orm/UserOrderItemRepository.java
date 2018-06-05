package com.github.bogdanovmn.boardgameorder.web.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserOrderItemRepository extends JpaRepository<UserOrderItem, Integer> {
	UserOrderItem findFirstByUserAndItem(User user, Item item);

	List<UserOrderItem> getAllByUser(User user);
}