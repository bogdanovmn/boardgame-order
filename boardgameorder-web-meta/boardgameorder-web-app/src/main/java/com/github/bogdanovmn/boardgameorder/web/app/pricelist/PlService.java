package com.github.bogdanovmn.boardgameorder.web.app.pricelist;

import com.github.bogdanovmn.boardgameorder.web.app.SourceService;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPrice;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPriceChange;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.ItemPriceChangeRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.Source;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.User;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserOrderItem;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.UserOrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class PlService {
    private final UserOrderItemRepository userOrderItemRepository;
    private final ItemPriceChangeRepository itemPriceChangeRepository;
    private final SourceService sourceService;

    PlService(
        final UserOrderItemRepository userOrderItemRepository,
        final ItemPriceChangeRepository itemPriceChangeRepository,
        final SourceService sourceService
    ) {
        this.userOrderItemRepository = userOrderItemRepository;
        this.itemPriceChangeRepository = itemPriceChangeRepository;
        this.sourceService = sourceService;
    }

    PlView actualPriceListView(User user) {
        List<ItemPrice> itemPrices = sourceService.actualPrices();
        List<UserOrderItem> userOrderItems = userOrderItemRepository.getAllByUser(user);

        return new PlView(itemPrices, userOrderItems);
    }

    PlView priceListView(Integer sourceId) {
        List<ItemPrice> itemPrices = sourceService.prices(sourceId);
        return new PlView(itemPrices);
    }

    PlChangesView priceListLastChangesView(PlChangesFilter filter) {
        ItemPriceChange itemPriceChange = itemPriceChangeRepository.findFirstByOrderBySourceIdDesc();
        return new PlChangesView(
            itemPriceChangeRepository.findAllBySourceId(
                itemPriceChange == null ? 0 : itemPriceChange.getSource().getId()),
            filter
        );
    }

    PlChangesView priceListChangesView(Integer sourceId, PlChangesFilter filter) {
        return new PlChangesView(
            itemPriceChangeRepository.findAllBySourceId(sourceId),
            filter
        );
    }

    List<Source> history() {
        return sourceService.allSources();
    }
}
