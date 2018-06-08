package com.github.bogdanovmn.boardgameorder.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PriceListColumnMapTest {

	@Test
	public void isLooksLikeNameColumn() {
		PriceListColumnMap map = new PriceListColumnMap();
		map.put("Номенклатура", 1);
		map.put("Цена", 2);
		map.put("Кол-во", 3);
		map.put("Штрих", 4);

		assertTrue(
			map.isLooksLikeNameColumn("номенклатура")
		);
	}

	@Test
	public void getNameIndex() {
		PriceListColumnMap map = new PriceListColumnMap();
		map.put("Номенклатура", 666);
		map.put("Цена", 2);
		map.put("Кол-во", 3);
		map.put("Штрих", 4);

		assertEquals(
			666,
			(int) map.getNameIndex()
		);
	}
}