package com.github.bogdanovmn.boardgameorder.web.app.admin.pricelist;

import com.github.bogdanovmn.boardgameorder.core.PriceItem;
import com.github.bogdanovmn.boardgameorder.core.PriceListContent;
import com.github.bogdanovmn.boardgameorder.core.PriceListExcelFile;
import com.github.bogdanovmn.boardgameorder.web.orm.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class PriceListAdminService {
	private static final Logger LOG = LoggerFactory.getLogger(PriceListAdminService.class);

	private final SourceRepository sourceRepository;
	private final ItemRepository itemRepository;
	private final ItemPriceRepository itemPriceRepository;
	private final EntityFactory entityFactory;

	public PriceListAdminService(
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
	public synchronized Source upload(MultipartFile file)
		throws IOException, UploadDuplicateException
	{
		LOG.info("Import data from file");

		String fileMd5 = DigestUtils.md5DigestAsHex(file.getBytes());
		Source existingSource = this.sourceRepository.findFirstByContentHash(fileMd5);
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
		List<PriceItem> items;
		final Source source = new Source();
		try (PriceListExcelFile excelFile = new PriceListExcelFile(file.getInputStream()))
		{
			PriceListContent price = new PriceListContent(excelFile);
			items = price.boardGames();
			Date fileCreateDate = excelFile.createdDate();
			Date fileModifyDate = excelFile.modifiedDate();
			if (fileCreateDate == null) {
				fileCreateDate = fileModifyDate;
			}
			sourceRepository.save(
				source
					.setContentHash(fileMd5)
					.setItemsCount(items.size())
					.setFileCreateDate(fileCreateDate)
					.setFileModifyDate(fileModifyDate)
					.setImportDate(new Date())
			);
		}

		LOG.info("Load exists items");
		Map<String, Item> itemsMap = itemRepository.findAll().stream()
			.collect(Collectors.toMap(
				Item::getTitle, x -> x
			));

		LOG.info("Import items: {}", items.size());
		int newItems = 0;
		int updatedItems = 0;
		for (PriceItem priceItem : items) {
			Item item = itemsMap.get(priceItem.getTitle());
			if (item != null) {
				if (updateItem(item, priceItem, source)) {
					updatedItems++;
				}
			}
			else {
				LOG.info("New item: {}", priceItem);
				item = addItem(priceItem);
				newItems++;
			}
			itemPriceRepository.save(
				new ItemPrice()
					.setItem(item)
					.setSource(source)
					.setCount(priceItem.getCount())
					.setPrice(priceItem.getPrice())
			);
		}
		LOG.info("Import items done. New: {}, updated: {}", newItems, updatedItems);

		return source;
	}

	private Item addItem(PriceItem priceItem) {
		Item newItem = new Item()
			.setTitle(priceItem.getTitle())
			.setBarcode(priceItem.getBarcode())
			.setUrl(priceItem.getUrl())
			.setPublisher(
				getPersistentPublisher(priceItem.getGroup())
			);
		itemRepository.save(newItem);
		return newItem;
	}

	private boolean updateItem(Item item, PriceItem priceItem, Source source) {
		boolean updated = false;
		FormattedStringBuilder updateDetails = new FormattedStringBuilder();

		if (!Objects.equals(item.getUrl(), priceItem.getUrl())) {
			updateDetails.append("Item URL change: %s --> %s\n", item.getUrl(), priceItem.getUrl());
			item.setUrl(priceItem.getUrl());
			updated = true;
		}

		if (!Objects.equals(item.getBarcode(), priceItem.getBarcode())) {
			updateDetails.append("Item barcode change: %s --> %s\n", item.getBarcode(), priceItem.getBarcode());
			item.setBarcode(priceItem.getBarcode());
			updated = true;
		}

		if (!Objects.equals(item.getPublisher().getName(), priceItem.getGroup())) {
			updateDetails.append("Item publisher change: %s --> %s\n", item.getPublisher().getName(), priceItem.getGroup());
			item.setPublisher(
				getPersistentPublisher(priceItem.getGroup())
			);
			updated = true;
		}

		if (updated) {
			LOG.info("Item '{}' updated: \n%s", item.getTitle(), updateDetails);
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
