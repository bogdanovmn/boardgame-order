package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPriceChange;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPriceChangeType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class PlChangesFilter {
    private final Map<PlChangesFilterToggle, Boolean> toggleValue;
    private final String resourcePath;

    PlChangesFilter(String resourcePath, Map<PlChangesFilterToggle, Boolean> toggleValue) {
        this.toggleValue = toggleValue;
        this.resourcePath = resourcePath;
    }

    boolean apply(final ItemPriceChange change) {
        return (toggleValue.get(PlChangesFilterToggle.NEW) && change.getType() == ItemPriceChangeType.NEW)
            || (toggleValue.get(PlChangesFilterToggle.COUNT) && change.isCountChange())
            || (toggleValue.get(PlChangesFilterToggle.PRICE) && change.isPriceChange())
            || (toggleValue.get(PlChangesFilterToggle.DELETE) && change.getType() == ItemPriceChangeType.DELETE);
    }

    List<PlChangesFilterToggleMenuItem> getMenu() {
        return Arrays.stream(PlChangesFilterToggle.values())
            .map(toggle -> new PlChangesFilterToggleMenuItem(toggle, toggleValue))
            .collect(Collectors.toList());
    }
}
