package com.github.bogdanovmn.boardgameorder.web.app;

import java.util.ArrayList;
import java.util.List;

public class AdminMenu {
    private final String current;
    private List<MenuItem> items;
    private boolean isPrepared = false;

    AdminMenu(ITEM current) {
        this.current = current.name();
    }

    List<MenuItem> getItems() {
        prepare();

        for (MenuItem menuItem : items) {
            if (menuItem.is(current)) {
                menuItem.select();
            }

        }
        return items;
    }

    private void prepare() {
        if (!isPrepared) {
            items = new ArrayList<>();
            items.add(new MenuItem(ITEM.UPLOAD_PRICE_LIST.name(), "/admin/price-list", "Загрузить прайс"));
            items.add(new MenuItem(ITEM.AUTO_IMPORT.name(), "/admin/auto-imports", "История авто-импорта"));
            items.add(new MenuItem(ITEM.USER_LIST.name(), "/admin/users", "Пользователи"));
        }
        isPrepared = true;
    }

    public enum ITEM {
        UPLOAD_PRICE_LIST,
        AUTO_IMPORT,
        USER_LIST,
        NONE
    }
}
