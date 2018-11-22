package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.core.ExcelPriceItem;
import com.github.bogdanovmn.boardgameorder.core.PriceListContent;
import com.github.bogdanovmn.boardgameorder.core.PriceListExcelFile;
import com.github.bogdanovmn.boardgameorder.web.orm.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class PriceListImportService {
	private static final Logger LOG = LoggerFactory.getLogger(PriceListImportService.class);

	private final SourceRepository sourceRepository;
	private final ItemRepository itemRepository;
	private final ItemPriceRepository itemPriceRepository;
	private final EntityFactory entityFactory;

	public PriceListImportService(
		SourceRepository sourceRepository,
		ItemRepository itemRepository,
		ItemPriceRepository itemPriceRepository,
		EntityFactory entityFactory
	) {
		this.sourceRepository = sourceRepository;
		this.itemRepository = itemRepository;
		this.itemPriceRepository = itemPriceRepository;
		this.entityFactory = entityFactory;
	}

	@Transactional(rollbackFor = Exception.class)
	public synchronized Source importFile(byte[] fileData, ImportType type)
		throws IOException, UploadDuplicateException
	{
		LOG.info("Import data from file ({})", type);

		String fileMd5 = DigestUtils.md5DigestAsHex(fileData);
		Source existingSource = sourceRepository.findFirstByContentHash(fileMd5);
		if (existingSource != null) {
			throw new UploadDuplicateException(
				String.format(
					"Такой прайс уже загружен: %s (md5: %s)",
						existingSource.toString(),
						existingSource.getContentHash()
				)
			);
		}

		LOG.info("Parse Excel file");
		List<ExcelPriceItem> items;
		final Source source = new Source();
		try (PriceListExcelFile excelFile = new PriceListExcelFile(new ByteArrayInputStream(fileData)))
		{
			PriceListContent price = new PriceListContent(excelFile);
			items = price.boardGames();
			Date fileModifyDate = excelFile.modifiedDate();
			Date currentDate = new Date();
			sourceRepository.save(
				source
					.setContentHash(fileMd5)
					.setItemsCount(items.size())
					.setFileModifyDate(fileModifyDate == null ? currentDate : fileModifyDate)
					.setImportDate(currentDate)
					.setImportType(type)
			);
		}
		catch (InvalidFormatException e) {
			throw new IOException(e);
		}

		LOG.info("Load exists items");
		ItemsMap itemsMap = new ItemsMap(itemRepository.findAll());

		LOG.info("Import items: {}", items.size());
		int newItems = 0;
		int updatedItems = 0;
		for (ExcelPriceItem excelPriceItem : items) {
			LOG.info("Excel Item: {}", excelPriceItem);

			if (excelPriceItem.getPrice() == null) {
				LOG.warn("Price is empty, skip it");
				continue;
			}
			if (excelPriceItem.getCount() == null) {
				LOG.warn("Count is empty, skip it");
				continue;
			}

			Item item = itemsMap.get(excelPriceItem);
			if (item != null) {
				LOG.info("Found Item: {}", item);
				if (updateItem(item, excelPriceItem)) {
					updatedItems++;
				}
			}
			else {
				LOG.info("New item: {}", excelPriceItem);
				item = addItem(excelPriceItem);
				newItems++;
			}
			itemPriceRepository.save(
				new ItemPrice()
					.setItem(item)
					.setSource(source)
					.setCount(excelPriceItem.getCount())
					.setPrice(excelPriceItem.getPrice())
			);
		}
		LOG.info("Import items done. New: {}, updated: {}", newItems, updatedItems);

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

	private boolean updateItem(Item item, ExcelPriceItem excelPriceItem) {
		boolean updated = false;
		FormattedStringBuilder updateDetails = new FormattedStringBuilder();

		if (!Objects.equals(item.getTitle(), excelPriceItem.getTitle())) {
			updateDetails.append("Item title change: %s --> %s\n", item.getTitle(), excelPriceItem.getTitle());
			item.setTitle(excelPriceItem.getTitle());
			updated = true;
		}

		if (!Objects.equals(item.getUrl(), excelPriceItem.getUrl())) {
			updateDetails.append("Item URL change: %s --> %s\n", item.getUrl(), excelPriceItem.getUrl());
			item.setUrl(excelPriceItem.getUrl());
			updated = true;
		}

		if (!Objects.equals(item.getBarcode(), excelPriceItem.getBarcode())) {
			updateDetails.append("Item barcode change: %s --> %s\n", item.getBarcode(), excelPriceItem.getBarcode());
			item.setBarcode(excelPriceItem.getBarcode());
			updated = true;
		}

		if (!Objects.equals(item.getPublisher().getName(), excelPriceItem.getGroup())) {
			updateDetails.append("Item publisher change: %s --> %s\n", item.getPublisher().getName(), excelPriceItem.getGroup());
			item.setPublisher(
				getPersistentPublisher(excelPriceItem.getGroup())
			);
			updated = true;
		}

		if (updated) {
			LOG.info("Change item {}: \n{}", item, updateDetails);
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
