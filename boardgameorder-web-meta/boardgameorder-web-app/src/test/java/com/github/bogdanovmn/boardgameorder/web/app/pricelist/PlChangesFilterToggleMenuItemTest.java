package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.bogdanovmn.boardgameorder.web.app.pricelist.PlChangesFilterToggle.*;
import static org.junit.Assert.*;

public class PlChangesFilterToggleMenuItemTest {

	private final static Map<PlChangesFilterToggle, Boolean> TOGGLES = new HashMap<PlChangesFilterToggle, Boolean>() {{
		put(NEW, true);
		put(DELETE, false);
		put(PRICE, true);
		put(COUNT, false);
	}};

	@Test
	public void title() {
		assertEquals(
			"count",
			new PlChangesFilterToggleMenuItem(COUNT, TOGGLES).title()
		);
	}

	@Test
	public void selected() {
		assertFalse(
			new PlChangesFilterToggleMenuItem(COUNT, TOGGLES).selected()
		);
		assertTrue(
			new PlChangesFilterToggleMenuItem(NEW, TOGGLES).selected()
		);
	}

	@Test
	public void filterParams() {
		assertEquals(
			"new=true&delete=false&price=true&count=true",
			new PlChangesFilterToggleMenuItem(COUNT, TOGGLES).filterParams()
		);
	}
}