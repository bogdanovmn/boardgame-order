package com.github.bogdanovmn.boardgameorder.cli.parser;


import com.github.bogdanovmn.boardgameorder.core.PriceListContent;
import com.github.bogdanovmn.boardgameorder.core.PriceListExcelFile;
import com.github.bogdanovmn.cmdline.CmdLineAppBuilder;

import java.io.FileInputStream;

public class App {
    public static void main(String[] args) throws Exception {
        new CmdLineAppBuilder(args)
            .withJarName("parser")
            .withDescription("Parse Excel price list file and print total info")
            .withRequiredArg("source", "data source file")
            .withEntryPoint(
                cmdLine -> {
                    try (
                        PriceListExcelFile file = new PriceListExcelFile(
                            new FileInputStream(cmdLine.getOptionValue("s"))
                        )
                    ) {
                        PriceListContent price = new PriceListContent(file);
                        price.printTotal();
                    }

                }
            ).build().run();
    }
}
