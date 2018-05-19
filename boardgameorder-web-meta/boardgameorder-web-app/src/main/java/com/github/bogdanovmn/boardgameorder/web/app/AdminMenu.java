package com.github.bogdanovmn.boardgameorder.web.app;

import java.util.ArrayList;
import java.util.List;

public class AdminMenu {
	public enum ITEM {
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
//			items.add(new MenuItem(ITEM.IMPORT.name(), "/admin/some", "..."));
		}
		isPrepared = true;
	}
}
