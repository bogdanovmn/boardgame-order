package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

class PlChangesFilterToggleMenuItem {
    private final PlChangesFilterToggle toggle;
    private final Map<PlChangesFilterToggle, Boolean> toggleValue;

    PlChangesFilterToggleMenuItem(final PlChangesFilterToggle toggle, final Map<PlChangesFilterToggle, Boolean> toggleValue) {
        this.toggle = toggle;
        this.toggleValue = toggleValue;
    }

    String title() {
        return toggle.title();
    }

    boolean selected() {
        return toggleValue.get(toggle);
    }

    String filterParams() {
        return toggleValue.entrySet().stream()
            .sorted(
                Comparator.comparingInt(x -> x.getKey().ordinal())
            )
            .map(x ->
                String.format("%s=%s",
                    x.getKey(),
                    x.getKey() == toggle
                        ? !x.getValue()
                        : x.getValue()
                )
            )
            .collect(Collectors.joining("&"));
    }
}
