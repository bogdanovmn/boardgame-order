package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.web.etl.PriceListFileOnDisk;
import com.github.bogdanovmn.boardgameorder.web.etl.PriceListImportService;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.AutoImport;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.AutoImportRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.AutoImportStatus;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ImportType;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Source;
import com.github.bogdanovmn.httpclient.simple.SimpleHttpClient;
import com.google.common.io.ByteStreams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Service
@Slf4j
class PriceListDownloadService {

    @Value("${price-list-dir}")
    private String priceListDir;

    private static final String PRICE_URL = "http://www.bambytoys.ru/bamby.xls";
    private final SimpleHttpClient httpClient = new SimpleHttpClient();

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
    public void autoImport() {
        LOG.info("Start check for updates");

        AutoImport currentAutoImport = new AutoImport().setImportDate(new Date());

        AutoImport lastImport = autoImportRepository.findTopByStatusOrderByIdDesc(AutoImportStatus.DONE);
        Date currentModifiedDate = null;
        try {
            currentModifiedDate = httpClient.getLastModified(PRICE_URL);
            LOG.info("Get last modified date success: {}", currentModifiedDate);
        } catch (IOException e) {
            String errorMsg = "Get last modified date error: " + e.getMessage();
            LOG.error(errorMsg);
            currentAutoImport.setErrorMsg(errorMsg);
        }

        if (currentModifiedDate != null) {
            currentAutoImport.setFileModifyDate(currentModifiedDate);
            if (lastImport == null || lastImport.getFileModifyDate().compareTo(currentModifiedDate) < 0) {
                LOG.info("Downloading price list...");
                try (InputStream fileStream = httpClient.downloadFile(PRICE_URL)) {
                    LOG.info("Downloading completed, start import");
                    byte[] fileData = ByteStreams.toByteArray(fileStream);
                    Source source = priceListImportService.importFile(fileData, ImportType.AUTO);

                    LOG.info("Import done, saving original file");
                    new PriceListFileOnDisk(currentModifiedDate, priceListDir)
                        .save(fileData);

                    LOG.info("All is OK, update changes between prices");
                    currentAutoImport.setStatus(AutoImportStatus.DONE)
                        .setSource(source);
                    priceListChangesService.updateLastChanges();
                } catch (Exception e) {
                    LOG.error(e.getMessage(), e);
                    currentAutoImport.setErrorMsg(e.getMessage());
                }
            } else if (lastImport.getFileModifyDate().compareTo(currentModifiedDate) == 0) {
                LOG.info("Last modified date is same: {}, skip it", currentModifiedDate);
                currentAutoImport.setStatus(AutoImportStatus.NO_CHANGES);
            }
        }

        autoImportRepository.save(currentAutoImport);

        LOG.info("Finish check for updates");
    }
}
