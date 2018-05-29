package com.github.bogdanovmn.boardgameorder.web.orm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemPriceTest {

	@Test
	public void getRoundedPrice() {
		assertEquals(
			123,
			new ItemPrice().setPrice(123.4).getRoundedPrice()
		);

		assertEquals(
			124,
			new ItemPrice().setPrice(123.5).getRoundedPrice()
		);
	}
}