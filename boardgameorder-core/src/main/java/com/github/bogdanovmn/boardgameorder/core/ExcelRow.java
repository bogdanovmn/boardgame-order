package com.github.bogdanovmn.boardgameorder.core;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

class ExcelRow {
	private final Row row;

	public ExcelRow(Row row) {

		this.row = row;
	}

	Cell cell(int i) {
		return row.getCell(i);
	}

	String cellStringValue(Integer cellNum) {
		ExcelCell cell = new ExcelCell(row.getCell(cellNum));
		return cell.isBlank()
			? null
			: cell.stringValue();

	}

	Double cellNumberValue(Integer cellNum) {
		ExcelCell cell = new ExcelCell(row.getCell(cellNum));
		return cell.isBlank()
			? null
			: cell.numberValue();
	}

	String cellUrlValue(Integer cellNum) {
		ExcelCell cell = new ExcelCell(row.getCell(cellNum));
		return cell.isBlank()
			? null
			: cell.urlValue();
	}
}
