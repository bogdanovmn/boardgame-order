package com.github.bogdanovmn.boardgameorder.cli.rebase;

import com.github.bogdanovmn.boardgameorder.web.etl.InvalidFileNameException;
import com.github.bogdanovmn.boardgameorder.web.etl.PriceListFileOnDisk;
import com.github.bogdanovmn.boardgameorder.web.etl.PriceListImportService;
import com.github.bogdanovmn.boardgameorder.web.etl.UploadDuplicateException;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
class RebaseService {
	private static final Logger LOG = LoggerFactory.getLogger(RebaseService.class);

	private final PriceListImportService importService;
	private final AutoImportRepository autoImportRepository;
	private final ItemPriceRepository itemPriceRepository;
	private final ItemPriceChangeRepository itemPriceChangeRepository;
	private final SourceRepository sourceRepository;
	private final ItemRepository itemRepository;
	private final UserOrderItemRepository userOrderItemRepository;
	private final PublisherRepository publisherRepository;

	@Autowired
	RebaseService(PriceListImportService importService,
				  AutoImportRepository autoImportRepository,
				  ItemPriceRepository itemPriceRepository,
				  ItemPriceChangeRepository itemPriceChangeRepository,
				  SourceRepository sourceRepository,
				  ItemRepository itemRepository,
				  UserOrderItemRepository userOrderItemRepository,
				  PublisherRepository publisherRepository
	) {
		this.importService = importService;
		this.autoImportRepository = autoImportRepository;
		this.itemPriceRepository = itemPriceRepository;
		this.itemPriceChangeRepository = itemPriceChangeRepository;
		this.sourceRepository = sourceRepository;
		this.itemRepository = itemRepository;
		this.userOrderItemRepository = userOrderItemRepository;
		this.publisherRepository = publisherRepository;
	}

	@Transactional(rollbackFor = Exception.class)
	public void rebase(String sourceDir, boolean cleanup) throws IOException, UploadDuplicateException, InvalidFileNameException {
		LOG.info("Starting rebase process");
		if (cleanup) {
			LOG.info("Cleanup old price lists data");
			cleanup();
			LOG.info("Cleanup is done");
		}

		PriceListArchive archive = new PriceListArchive(sourceDir);
		for (PriceListFileOnDisk archiveFile : archive.files()) {
			LOG.info("Import price list by {}", archiveFile.date());
			importService.importFile(
				archiveFile.bytes(),
				ImportType.REBASE,
				archiveFile.date()
			);
		}
	}

	private void cleanup() {
		autoImportRepository.deleteAll();
		itemPriceRepository.deleteAll();
		itemPriceChangeRepository.deleteAll();
		sourceRepository.deleteAll();
		publisherRepository.deleteAll();
		itemRepository.deleteAll();
		userOrderItemRepository.deleteAll();
	}
}
