package com.github.bogdanovmn.boardgameorder.web.etl;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PriceListFileOnDisk {
    private static final Pattern ARCHIVE_PATTERN = Pattern.compile("^price-list--(\\d{4}-\\d{2}-\\d{2}).xls$");
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final Date importDate;
    private final String path;

    public PriceListFileOnDisk(Date importDate, String path) {
        this.importDate = importDate;
        this.path = path;
    }

    public static PriceListFileOnDisk of(File file) throws InvalidFileNameException {
        Matcher fileMatcher = ARCHIVE_PATTERN.matcher(file.getName());
        if (fileMatcher.matches()) {
            try {
                Date date = DATE_FORMAT.parse(
                    fileMatcher.group(1)
                );
                return new PriceListFileOnDisk(date, file.getParent());
            } catch (ParseException e) {
                throw new InvalidFileNameException("Parse date error: " + file.toString());
            }
        }
        throw new InvalidFileNameException("File name not matched: " + file.toString());
    }

    public void save(byte[] fileData) throws IOException {
        Path fileName = fileName();
        LOG.info("Save file to {}", fileName);
        Files.createDirectories(Paths.get(path));
        Files.write(fileName, fileData);
    }

    private Path fileName() {
        return Paths.get(
            path,
            String.format(
                "price-list--%s.xls",
                DATE_FORMAT.format(importDate)
            )
        );
    }

    public byte[] bytes() throws IOException {
        return Files.readAllBytes(fileName());
    }

    public Date date() {
        return importDate;
    }
}
