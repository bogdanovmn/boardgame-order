package com.github.bogdanovmn.boardgameorder.web.etl;

import com.github.bogdanovmn.boardgameorder.core.ExcelPriceItem;
import com.github.bogdanovmn.boardgameorder.core.PriceListContent;
import com.github.bogdanovmn.boardgameorder.core.PriceListExcelFile;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ImportType;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Item;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPriceRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Publisher;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Source;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.SourceRepository;
import com.github.bogdanovmn.common.core.BigString;
import com.github.bogdanovmn.common.spring.jpa.EntityFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class PriceListImportService {
    private final SourceRepository sourceRepository;
    private final ItemRepository itemRepository;
    private final ItemPriceRepository itemPriceRepository;
    private final EntityFactory entityFactory;

    @Transactional(rollbackFor = Exception.class)
    public Source importFile(byte[] fileData, ImportType type) throws IOException, UploadDuplicateException {
        return importFile(fileData, type, null);
    }

    @Transactional(rollbackFor = Exception.class)
    public synchronized Source importFile(byte[] fileData, ImportType type, Date customDate)
        throws IOException, UploadDuplicateException {
        LOG.info("Import data from file ({})", type);

        String fileMd5 = DigestUtils.md5DigestAsHex(fileData);
        Source existingSource = sourceRepository.findFirstByContentHash(fileMd5);
        if (existingSource != null) {
            throw new UploadDuplicateException(
                String.format(
                    "Такой прайс уже загружен: %s (md5: %s)",
                    existingSource,
                    existingSource.getContentHash()
                )
            );
        }

        LOG.info("Parse Excel file");
        List<ExcelPriceItem> itemsToImport;
        final Source source = new Source();
        try (PriceListExcelFile excelFile = new PriceListExcelFile(new ByteArrayInputStream(fileData))) {
            PriceListContent price = new PriceListContent(excelFile);
            itemsToImport = price.boardGames();
            Date fileModifyDate = excelFile.modifiedDate();
            Date currentDate = customDate != null ? customDate : new Date();
            sourceRepository.save(
                source
                    .setContentHash(fileMd5)
                    .setItemsCount(itemsToImport.size())
                    .setFileModifyDate(fileModifyDate == null ? currentDate : fileModifyDate)
                    .setImportDate(currentDate)
                    .setImportType(type)
            );
        } catch (InvalidFormatException e) {
            throw new IOException(e);
        }

        LOG.info("Load exists items");
        ItemsMap previousItemsMap = new ItemsMap(itemRepository.findAll());

        LOG.info("Import items: {}", itemsToImport.size());
        int newItems = 0;
        int updatedItems = 0;
        int noPriceItems = 0;
        int noCountItems = 0;
        int duplicates = 0;
        Set<Integer> processedPriceItems = new HashSet<>();
        for (ExcelPriceItem excelPriceItem : itemsToImport) {
            LOG.debug("Excel Item: {}", excelPriceItem);

            if (excelPriceItem.getPrice() == null) {
                LOG.warn("Price is empty, skip it");
                noPriceItems++;
                continue;
            }
            if (excelPriceItem.getCount() == null) {
                LOG.warn("Count is empty, skip it");
                noCountItems++;
                continue;
            }

            Item storedItem = previousItemsMap.get(excelPriceItem);
            if (storedItem != null) {
                LOG.debug("Found item: {}", storedItem);
                if (processedPriceItems.contains(storedItem.getId())) {
                    LOG.warn("Duplicate: {} with {}", excelPriceItem.getTitle(), storedItem.getTitle());
                    duplicates++;
                    continue;
                } else {
                    if (updateItem(storedItem, excelPriceItem)) {
                        updatedItems++;
                    }
                }
            } else {
                LOG.debug("New item: {}", excelPriceItem);
                storedItem = addItem(excelPriceItem);
                newItems++;
            }
            itemPriceRepository.save(
                new ItemPrice()
                    .setItem(storedItem)
                    .setSource(source)
                    .setCount(excelPriceItem.getCount())
                    .setPrice(excelPriceItem.getPrice())
            );
            processedPriceItems.add(storedItem.getId());
        }
        LOG.info(
            "Import items done. New: {}, updated: {}, no price: {}, no count: {}, duplicates {}",
            newItems, updatedItems, noPriceItems, noCountItems, duplicates
        );

        return source;
    }

    private Item addItem(ExcelPriceItem excelPriceItem) {
        Item newItem = new Item()
            .setTitle(excelPriceItem.getTitle())
            .setBarcode(excelPriceItem.getBarcode())
            .setUrl(excelPriceItem.getUrl())
            .setPublisher(
                getPersistentPublisher(excelPriceItem.getGroup())
            );
        itemRepository.save(newItem);
        return newItem;
    }

    private boolean updateItem(Item current, ExcelPriceItem newest) {
        boolean updated = false;
        BigString updateDetails = new BigString();

        if (!Objects.equals(current.getTitle(), newest.getTitle())) {
            updateDetails.addLine("Item title change: %s --> %s", current.getTitle(), newest.getTitle());
            current.setTitle(newest.getTitle());
            updated = true;
        }

        if (!Objects.equals(current.getUrl(), newest.getUrl())) {
            updateDetails.addLine("Item URL change: %s --> %s", current.getUrl(), newest.getUrl());
            current.setUrl(newest.getUrl());
            updated = true;
        }

        if (!Objects.equals(current.getBarcode(), newest.getBarcode())) {
            updateDetails.addLine("Item barcode change: %s --> %s", current.getBarcode(), newest.getBarcode());
            current.setBarcode(newest.getBarcode());
            updated = true;
        }

        if (!Objects.equals(current.getPublisher().getName(), newest.getGroup())) {
            updateDetails.addLine("Item publisher change: %s --> %s", current.getPublisher().getName(), newest.getGroup());
            current.setPublisher(
                getPersistentPublisher(newest.getGroup())
            );
            updated = true;
        }

        if (updated) {
            LOG.info("Change item {}: \n{}", current, updateDetails);
        }
        return updated;
    }

    private Publisher getPersistentPublisher(final String publisherName) {
        return entityFactory.getPersistBaseEntityWithUniqueName(
            (Publisher) new Publisher()
                .setName(publisherName)
        );
    }
}
