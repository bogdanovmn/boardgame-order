package com.github.bogdanovmn.boardgameorder.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ExcelPriceItemTest {

	@Test
	public void getGroup() {
		assertEquals(
			"\"Bondibon\"",
			new ExcelPriceItem("66.1 \"Bondibon\"", "title", 1.23, 10., "1234567", "http://blabla.ru")
				.getGroup()
		);

		assertEquals(
			"\"Bondibon\"",
			new ExcelPriceItem("66 \"Bondibon\"", "title", 1.23, 10., "1234567", "http://blabla.ru")
				.getGroup()
		);
	}
}