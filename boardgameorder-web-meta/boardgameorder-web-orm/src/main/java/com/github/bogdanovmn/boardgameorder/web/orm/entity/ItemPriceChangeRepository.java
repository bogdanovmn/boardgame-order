package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface ItemPriceChangeRepository extends JpaRepository<ItemPriceChange, Integer> {
    @EntityGraph(attributePaths = {"item", "item.publisher"})
    List<ItemPriceChange> findAllBySourceId(Integer sourceId);

    ItemPriceChange findFirstByOrderBySourceIdDesc();

    @Modifying
    void deleteAllBySource(Source source);
}
