package com.github.bogdanovmn.boardgameorder.core;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class PriceListExcelFileTest {
	private final String fileName;
	private PriceListExcelFile excelFile;

	public PriceListExcelFileTest(final String fileName) {
		this.fileName = fileName;
	}

	@Parameters(name = "{index} - {0}")
	public static Collection<Object[]> data() {
		return Arrays.asList(
			new Object[][] {
				{ "price-2.xlsx" },
				{ "price-3.xls" }
			}
		);
	}

	@Before
	public void setUp() throws IOException, InvalidFormatException {
		excelFile = new PriceListExcelFile(
			getClass().getClassLoader().getResourceAsStream(fileName)
		);
	}
	@After
	public void setDown() throws IOException {
		if (excelFile != null) excelFile.close();
	}

	@Test @Ignore
	public void createdDate() throws IOException {
		assertEquals(
			"Fri Jun 08 22:18:34 MSK 2018",
			excelFile.createdDate().toString()
		);
	}

	@Test @Ignore
	public void modifiedDate() throws IOException {
		assertEquals(
			"Fri Jun 08 22:18:34 MSK 2018",
			excelFile.modifiedDate().toString()
		);
	}

	@Test
	public void priceItems() {
		assertEquals(
			38,
			excelFile.priceItems().size()
		);
	}
}