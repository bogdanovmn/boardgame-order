package com.github.bogdanovmn.boardgameorder.web.orm;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ItemTest {

	@Test
	public void getHtmlTitle() {
		assertEquals(
			"blabla <b>\"Game Name\"</b> blabla",
			new Item().setTitle("blabla \"Game Name\" blabla").getHtmlTitle()
		);

		assertEquals(
			"Наст.игра МХ <b>\"Pathfinder \"Возвращение Рунных Властителей.\"</b> арт.1424",
			new Item().setTitle("Наст.игра МХ \"Pathfinder \"Возвращение Рунных Властителей.\" арт.1424").getHtmlTitle()
		);
	}
}