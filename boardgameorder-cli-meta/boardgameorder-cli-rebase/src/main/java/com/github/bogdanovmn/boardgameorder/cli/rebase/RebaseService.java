package com.github.bogdanovmn.boardgameorder.cli.rebase;

import com.github.bogdanovmn.boardgameorder.web.etl.InvalidFileNameException;
import com.github.bogdanovmn.boardgameorder.web.etl.PriceListFileOnDisk;
import com.github.bogdanovmn.boardgameorder.web.etl.PriceListImportService;
import com.github.bogdanovmn.boardgameorder.web.etl.UploadDuplicateException;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.AutoImportRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ImportType;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPriceChangeRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPriceRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.PublisherRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.SourceRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserOrderItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
class RebaseService {
    private final PriceListImportService importService;
    private final AutoImportRepository autoImportRepository;
    private final ItemPriceRepository itemPriceRepository;
    private final ItemPriceChangeRepository itemPriceChangeRepository;
    private final SourceRepository sourceRepository;
    private final ItemRepository itemRepository;
    private final UserOrderItemRepository userOrderItemRepository;
    private final PublisherRepository publisherRepository;

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
