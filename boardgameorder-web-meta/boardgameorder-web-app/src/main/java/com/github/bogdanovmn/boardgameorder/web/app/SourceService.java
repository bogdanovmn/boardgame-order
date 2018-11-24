package com.github.bogdanovmn.boardgameorder.web.app;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPriceRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Source;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.SourceRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SourceService {
	private final SourceRepository sourceRepository;
	private final ItemPriceRepository itemPriceRepository;

	public SourceService(final SourceRepository sourceRepository, final ItemPriceRepository itemPriceRepository) {
		this.sourceRepository = sourceRepository;
		this.itemPriceRepository = itemPriceRepository;
	}

	private Source actualSource() {
		return sourceRepository.findTopByOrderByFileModifyDateDesc();
	}

	public List<ItemPrice> actualPrices() {
		return itemPriceRepository.findBySourceId(
			actualSource().getId()
		);
	}

	public List<ItemPrice> prices(Integer sourceId) {
		return itemPriceRepository.findBySourceId(sourceId);
	}

	public List<Source> allSources() {
		return sourceRepository.findAllByOrderByFileModifyDateDesc();
	}
}
