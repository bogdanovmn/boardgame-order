package com.github.bogdanovmn.boardgameorder.core;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PriceListExcelFile implements Closeable {
	private final static String IH_NAME    = "Номенклатура";
	private final static String IH_COUNT   = "Кол-во";
	private final static String IH_PRICE   = "базовая цена";
	private final static String IH_BARCODE = "Штрих";
	private final static String IH_FOTO    = "ФОТО";

	private final HSSFWorkbook excelBook;

	private int itemsRowBegin;
	private int itemsRowEnd;

	private Map<String, Integer> columnMap = new HashMap<>();

	public PriceListExcelFile(HSSFWorkbook excelBook) {
		this.excelBook = excelBook;
	}

	public PriceListExcelFile(InputStream source) throws IOException {
		this(
			new HSSFWorkbook(source)
		);
	}

	private void findMeta() {
		boolean itemsRangeNext = false;
		for (Row row : excelBook.getSheetAt(0)) {
			boolean itemsHeaderRow = false;

			for (Cell cell : row) {
				ExcelCell ec = new ExcelCell(cell);

				if (ec.isFormula() && itemsRangeNext) {
					List<Integer> range = ec.getSumRange();
					itemsRowBegin = range.get(0);
					itemsRowEnd   = range.get(1);
					itemsRangeNext = false;
				}

				if (itemsHeaderRow) {
					columnMap.put(cell.getStringCellValue(), cell.getColumnIndex());
				}

				if (ec.isContainString("Сумма заказа по базовым ценам")) {
					itemsRangeNext = true;
				}
				if (ec.isContainString(IH_NAME)) {
					itemsHeaderRow = true;
					columnMap.put(IH_NAME, cell.getColumnIndex());
				}
			}
		}
	}

	public void print() {
		findMeta();
		HSSFSheet sheet = excelBook.getSheetAt(0);


		int r = 0;
		for (Row row : sheet) {
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
			if (r > 200) break;
		}
	}

	@Override
	public void close() throws IOException {
		excelBook.close();
	}
}
