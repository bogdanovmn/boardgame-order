package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.github.bogdanovmn.boardgameorder.web.app.pricelist.PlChangesFilterToggle.COUNT;
import static com.github.bogdanovmn.boardgameorder.web.app.pricelist.PlChangesFilterToggle.DELETE;
import static com.github.bogdanovmn.boardgameorder.web.app.pricelist.PlChangesFilterToggle.NEW;
import static com.github.bogdanovmn.boardgameorder.web.app.pricelist.PlChangesFilterToggle.PRICE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
            "Изменения в кол-ве",
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