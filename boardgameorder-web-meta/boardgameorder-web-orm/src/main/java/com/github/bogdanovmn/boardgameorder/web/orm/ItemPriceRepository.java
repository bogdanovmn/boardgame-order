package com.github.bogdanovmn.boardgameorder.web.orm;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemPriceRepository extends JpaRepository<ItemPrice, Integer> {
	@EntityGraph(attributePaths = {"item", "item.publisher"})
	List<ItemPrice> findBySource(Source source);
}
