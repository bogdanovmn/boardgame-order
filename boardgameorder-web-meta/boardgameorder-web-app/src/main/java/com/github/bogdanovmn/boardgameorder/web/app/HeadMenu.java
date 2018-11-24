package com.github.bogdanovmn.boardgameorder.web.app;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.User;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserRole;

import java.util.ArrayList;
import java.util.List;

public class HeadMenu {
	public final static String DEFAULT_PAGE = "/price-lists/last";

	public enum ITEM {
		PRICE_LIST,
		PRICE_LIST_CHANGES,
		ORDER,
		SETTINGS,
		PRICE_LIST_HISTORY,
		INVITE,
		ADMIN
	}

	private final String current;
	private final User user;
	private List<MenuItem> items;
	private boolean isPrepared = false;

	HeadMenu(ITEM current, User user) {
		this.current = current.name();
		this.user = user;
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
		if (!this.isPrepared) {
			items = new ArrayList<>();
			items.add(new MenuItem(ITEM.PRICE_LIST.name(), "/price-lists/last", "Прайс лист"));
			items.add(new MenuItem(ITEM.PRICE_LIST_CHANGES.name(), "/price-lists/last/changes", "Что новенького?"));
			items.add(new MenuItem(ITEM.ORDER.name(), "/user/order/items", "Корзина"));
			items.add(new MenuItem(ITEM.PRICE_LIST_HISTORY.name(), "/price-lists", "История прайсов"));
			if (user.hasRole(UserRole.Type.Admin)) {
				items.add(new MenuItem(ITEM.ADMIN.name(), "/admin/price-list", "Админка"));
			}
			if (user.hasRole(UserRole.Type.Invite)) {
				items.add(new MenuItem(ITEM.INVITE.name(), "/invites", "Инвайты"));
			}
			this.isPrepared = true;
		}
	}
}
