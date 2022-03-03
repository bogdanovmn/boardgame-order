package com.github.bogdanovmn.boardgameorder.cli.rebase;

import com.github.bogdanovmn.boardgameorder.web.etl.InvalidFileNameException;
import com.github.bogdanovmn.boardgameorder.web.etl.PriceListFileOnDisk;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class PriceListArchive {
    private final String sourceDir;

    PriceListArchive(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    List<PriceListFileOnDisk> files() throws IOException, InvalidFileNameException {
        List<PriceListFileOnDisk> result = new LinkedList<>();
        File[] files = new File(this.sourceDir).listFiles();
        if (files == null) {
            throw new IOException("Directory not found: " + sourceDir);
        }

        for (File file : files) {
            result.add(
                PriceListFileOnDisk.of(file)
            );
        }

        result.sort(Comparator.comparing(PriceListFileOnDisk::date));

        return result;
    }
}
