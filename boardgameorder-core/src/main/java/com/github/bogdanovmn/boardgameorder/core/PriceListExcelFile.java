package com.github.bogdanovmn.boardgameorder.core;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class PriceListExcelFile implements Closeable {
	private final InputStream source;

	public PriceListExcelFile(InputStream source) {
		this.source = source;

	}

	@Override
	public void close() throws IOException {
		source.close();
	}
}
