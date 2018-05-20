package com.github.bogdanovmn.boardgameorder.core;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

public class PriceListExcelFile implements Closeable {
	private final InputStream source;

	public PriceListExcelFile(InputStream source) {
		this.source = source;

	}

	public void fetch() throws IOException {
		HSSFWorkbook myExcelBook = new HSSFWorkbook(source);
		HSSFSheet myExcelSheet = myExcelBook.getSheetAt(0);


		int r = 0;
		for (Row row : myExcelSheet) {
			System.out.println(
				String.format("----- %d -----", r++)
			);
			for (Cell cell : row) {
				System.out.println(
					String.format(
						"[%d %7s] %s",
							cell.getColumnIndex(),
							cell.getCellTypeEnum(),
							(cell.toString().equals("Фото")
								? cell.getHyperlink().getAddress()
								: cell)
					)
				);
			}
			if (r > 500) break;
		}
		myExcelBook.close();
	}

	@Override
	public void close() throws IOException {
		source.close();
	}
}
