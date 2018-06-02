package com.github.bogdanovmn.boardgameorder.web.app;

import java.util.ArrayList;
import java.util.List;

public class AdminMenu {
	public enum ITEM {
		UPLOAD_PRICE_LIST,
		NONE
	}

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
			items.add(new MenuItem(ITEM.UPLOAD_PRICE_LIST.name(), "admin/upload-price-list", "Загрузить прайс"));
		}
		isPrepared = true;
	}
}
