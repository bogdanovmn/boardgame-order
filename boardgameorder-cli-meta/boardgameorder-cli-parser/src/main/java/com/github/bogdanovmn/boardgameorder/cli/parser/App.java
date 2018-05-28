package com.github.bogdanovmn.boardgameorder.cli.parser;


import com.github.bogdanovmn.boardgameorder.core.PriceListContent;
import com.github.bogdanovmn.boardgameorder.core.PriceListExcelFile;
import com.github.bogdanovmn.cmdlineapp.CmdLineAppBuilder;

import java.io.FileInputStream;

public class App {
	public static void main(String[] args) throws Exception {
		new CmdLineAppBuilder(args)
			.withJarName("parser")
			.withDescription("Parse Excel price list file and print total info")
			.withArg("source", "data source file")
			.withEntryPoint(
				cmdLine -> {
					try (
						PriceListExcelFile file = new PriceListExcelFile(
							new FileInputStream(cmdLine.getOptionValue("s"))
						)
					) {
						PriceListContent price = new PriceListContent(file);
						price.printTotal();
						System.out.println(file.createdDate());
						System.out.println(file.modifiedDate());
					}

				}
			).build().run();
	}
}
