package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

enum PlChangesFilterToggle {
	NEW("Новинки"),
	DELETE("Удаленные позиции"),
	PRICE("Изменения в цене"),
	COUNT("Изменения в кол-ве");

	private final String title;

	PlChangesFilterToggle(final String title) {
		this.title = title;
	}

	String title() {
		return title;
	}
	@Override
	public String toString() {
		return this.name().toLowerCase();
	}
}
