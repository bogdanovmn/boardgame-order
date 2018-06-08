package com.github.bogdanovmn.boardgameorder.core;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class PriceListExcelFile implements Closeable {
	private final HSSFWorkbook excelBook;

	private int itemsRowBegin;
	private int itemsRowEnd;

	private PriceListColumnMap columnMap = new PriceListColumnMap();

	public PriceListExcelFile(HSSFWorkbook excelBook) {
		this.excelBook = excelBook;
	}

	public PriceListExcelFile(InputStream source) throws IOException {
		this(
			new HSSFWorkbook(source)
		);
	}

	public Date createdDate() {
		return excelBook.getSummaryInformation().getCreateDateTime();
	}

	public Date modifiedDate() {
		return excelBook.getSummaryInformation().getLastSaveDateTime();
	}

	private void findMeta() {
		boolean itemsRangeNext = false;
		boolean itemsHeaderRow = false;
		for (Row row : excelBook.getSheetAt(0)) {
			if (itemsHeaderRow) break;

//			printRow(row);

			for (Cell cell : row) {
				ExcelCell ec = new ExcelCell(cell);

				if (ec.isFormula() && itemsRangeNext) {
					List<Integer> range = ec.getSumRange();
					itemsRowBegin = range.get(0) - 1;
					itemsRowEnd   = range.get(1);
					itemsRangeNext = false;
				}

				if (itemsHeaderRow) {
					columnMap.put(ec.stringValue(), cell.getColumnIndex());
				}

				if (ec.isContainString("Сумма заказа по ")) {
					itemsRangeNext = true;
				}

				if (columnMap.isLooksLikeNameColumn(ec.stringValue())) {
					itemsHeaderRow = true;
					columnMap.put(ec.stringValue(), cell.getColumnIndex());
				}
			}
		}
	}

	List<PriceItem> priceItems() {
		List<PriceItem> result = new LinkedList<>();

		findMeta();

		HSSFSheet sheet = excelBook.getSheetAt(0);
		String currentGroup = null;
		for (int i = itemsRowBegin; i <= itemsRowEnd; i++) {
			Row rawRow = sheet.getRow(i);
			if (rawRow == null) {
				continue;
			}
			ExcelRow row = new ExcelRow(rawRow);

//			printRow(rawRow);

			if (new ExcelCell(row.cell(1)).isNumber()) {
				if (currentGroup == null) {
					throw new IllegalStateException("Row like price but without group");
				}
				result.add(
					new PriceItem(
						currentGroup,
						row.cellStringValue(columnMap.getNameIndex()),
						row.cellNumberValue(columnMap.getPriceIndex()),
						row.cellNumberValue(columnMap.getCountIndex()),
						row.cellStringValue(columnMap.getBarcodeIndex()),
						row.cellUrlValue(columnMap.getPhotoIndex())
					)
				);
			}
			else {
				currentGroup = row.cellStringValue(0);
			}
		}
		return result;
	}

	private void printRow(Row row) {
		System.out.println(
			String.format("----- %d -----", row.getRowNum())
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
	}

	public void printAll() {
		HSSFSheet sheet = excelBook.getSheetAt(0);

		int r = 0;
		for (Row row : sheet) {
			printRow(row);
			if (r++ > 500) break;
		}
	}

	@Override
	public void close() throws IOException {
		excelBook.close();
	}
}
