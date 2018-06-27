package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.orm.*;
import com.github.bogdanovmn.httpclient.simple.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Service
class PriceListDownloadService {
	private static final Logger LOG = LoggerFactory.getLogger(PriceListDownloadService.class);

	private final static String PRICE_URL = "http://www.bambytoys.ru/bamby.xls";
	private final SimpleHttpClient httpClient = new SimpleHttpClient("");

	private final AutoImportRepository autoImportRepository;
	private final PriceListImportService priceListImportService;
	private final PriceListChangesService priceListChangesService;


	PriceListDownloadService(
		final AutoImportRepository autoImportRepository,
		final PriceListImportService priceListImportService,
		final PriceListChangesService priceListChangesService
	) {
		this.autoImportRepository = autoImportRepository;
		this.priceListImportService = priceListImportService;
		this.priceListChangesService = priceListChangesService;
	}

	@Scheduled(fixedDelay = 9 * 3600 * 1000)
	void autoImport() {
		LOG.info("Start check for updates");

		AutoImport currentAutoImport = new AutoImport().setImportDate(new Date());

		AutoImport lastImport = autoImportRepository.findTopByStatusOrderByFileModifyDateDesc(AutoImportStatus.DONE);
		Date currentModifiedDate = null;
		try {
			currentModifiedDate = httpClient.getLastModified(PRICE_URL);
			LOG.info("Get last modified date success: {}", currentModifiedDate);
		}
		catch (IOException e) {
			String errorMsg = "Get last modified date error: " + e.getMessage();
			LOG.error(errorMsg);
			currentAutoImport.setErrorMsg(errorMsg);
		}

		if (currentModifiedDate != null) {
			currentAutoImport.setFileModifyDate(currentModifiedDate);
			if (lastImport == null || lastImport.getFileModifyDate().compareTo(currentModifiedDate) < 0) {
				try {
					LOG.info("Downloading price list...");
					InputStream fileStream = httpClient.downloadFile(PRICE_URL);
					LOG.info("Downloading completed, start import");
					Source source = priceListImportService.importFile(fileStream, ImportType.AUTO);
					currentAutoImport.setStatus(AutoImportStatus.DONE)
						.setSource(source);

					priceListChangesService.updateAllChanges();
				}
				catch (Exception e) {
					LOG.error(e.getMessage());
					currentAutoImport.setErrorMsg(e.getMessage());
				}
			}
			else if (lastImport.getFileModifyDate().compareTo(currentModifiedDate) == 0) {
				LOG.info("Last modified date is same: {}, skip it", currentModifiedDate);
				currentAutoImport.setStatus(AutoImportStatus.NO_CHANGES);
			}
		}

		autoImportRepository.save(currentAutoImport);

		LOG.info("Finish check for updates");
	}
}
