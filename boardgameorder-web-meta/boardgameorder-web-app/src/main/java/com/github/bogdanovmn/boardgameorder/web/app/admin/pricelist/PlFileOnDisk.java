package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

class PlFileOnDisk {
	private static final Logger LOG = LoggerFactory.getLogger(PlFileOnDisk.class);

	private final byte[] fileData;
	private final Date modifiedDate;
	private final String path;

	PlFileOnDisk(byte[] fileData, Date modifiedDate, String path) {
		this.fileData = fileData;
		this.modifiedDate = modifiedDate;
		this.path = path;
	}

	void save() throws IOException {
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
					new SimpleDateFormat("yyyy-MM-dd").format(modifiedDate)
			)
		);
	}
}
