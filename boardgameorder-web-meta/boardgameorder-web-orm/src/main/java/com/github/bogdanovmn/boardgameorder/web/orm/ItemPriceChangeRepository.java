package com.github.bogdanovmn.boardgameorder.web.orm;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPriceChangeRepository extends JpaRepository<ItemPriceChange, Integer> {
	@EntityGraph(attributePaths = {"item", "item.publisher"})
	List<ItemPriceChange> findAllBySourceId(Integer sourceId);

	ItemPriceChange findFirstByOrderBySourceIdDesc();
}
