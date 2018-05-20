package com.github.bogdanovmn.boardgameorder.core;

import java.io.IOException;

public class PriceListContent {
	private final PriceListExcelFile file;

	public PriceListContent(PriceListExcelFile file) {
		this.file = file;
	}

	private void fetch() {

	}
	public void printTotal() throws IOException {
		file.fetch();
	}


}
