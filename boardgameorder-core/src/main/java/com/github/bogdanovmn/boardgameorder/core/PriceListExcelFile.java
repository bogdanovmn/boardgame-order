package com.github.bogdanovmn.boardgameorder.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class PriceListExcelFile implements Closeable {
    private static final String DEFAULT_GROUP_NAME = "-- Noname --";
    private final Workbook excelBook;
    private final PriceListColumnMap columnMap = new PriceListColumnMap();
    private int itemsRowBegin;
    private int itemsRowEnd;

    private PriceListExcelFile(Workbook excelBook) {
        this.excelBook = excelBook;
    }

    public PriceListExcelFile(InputStream source) throws IOException, InvalidFormatException {
        this(
            WorkbookFactory.create(source)
        );
    }

    public Date createdDate() {
        if (excelBook instanceof XSSFWorkbook) {
            return ((XSSFWorkbook) excelBook).getProperties().getCoreProperties().getCreated();
        }
        if (excelBook instanceof HSSFWorkbook) {
            return ((HSSFWorkbook) excelBook).getSummaryInformation().getCreateDateTime();
        }
        throw new RuntimeException("Unknown Excel format");
    }

    public Date modifiedDate() {
        if (excelBook instanceof XSSFWorkbook) {
            return ((XSSFWorkbook) excelBook).getProperties().getCoreProperties().getModified();
        }
        if (excelBook instanceof HSSFWorkbook) {
            return ((HSSFWorkbook) excelBook).getSummaryInformation().getLastSaveDateTime();
        }
        throw new RuntimeException("Unknown Excel format");
    }

    private void findMeta() {
        boolean itemsRangeNext = false;
        boolean itemsHeaderRow = false;
        for (Row row : excelBook.getSheetAt(0)) {
            if (itemsHeaderRow) {
                break;
            }

//printRow(row);

            for (Cell cell : row) {
                ExcelCell ec = new ExcelCell(cell);

                if (ec.isFormula() && itemsRangeNext) {
                    List<Integer> range = ec.getSumRange();
                    itemsRowBegin = range.get(0) - 1;
                    itemsRowEnd = range.get(1);
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

    List<ExcelPriceItem> priceItems() {
        List<ExcelPriceItem> result = new LinkedList<>();

        findMeta();

        Sheet sheet = excelBook.getSheetAt(0);
        String currentGroup = null;
        for (int i = itemsRowBegin; i <= itemsRowEnd; i++) {
            Row rawRow = sheet.getRow(i);
            if (rawRow == null) {
                continue;
            }
            ExcelRow row = new ExcelRow(rawRow);

//printRow(rawRow);

            if (row.cell(1).isNumber()) {
                if (currentGroup == null) {
                    LOG.warn("Row like price but without group: {}", row);
                    currentGroup = DEFAULT_GROUP_NAME;
                }
                result.add(
                    new ExcelPriceItem(
                        currentGroup,
                        row.cellStringValue(columnMap.getNameIndex()),
                        row.cellNumberValue(columnMap.getPriceIndex()),
                        row.cellNumberValue(columnMap.getCountIndex()),
                        row.cellStringValue(columnMap.getBarcodeIndex()),
                        row.cellUrlValue(columnMap.getPhotoIndex())
                    )
                );
            } else {
                if (!row.cell(0).isBlank()) {
                    currentGroup = row.cellStringValue(0);
                }
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

//    public void printAll() {
//        Sheet sheet = excelBook.getSheetAt(0);
//
//        int r = 0;
//        for (Row row : sheet) {
//            printRow(row);
//            if (r++ > 500) {
//                break;
//            }
//        }
//    }

    @Override
    public void close() throws IOException {
        excelBook.close();
    }
}
