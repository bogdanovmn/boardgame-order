package com.github.bogdanovmn.boardgameorder.web.app.order;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import com.github.bogdanovmn.common.spring.mvc.ViewTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
class CartController extends AbstractVisualController {
    private final CartService cartService;
    private final OrderService orderService;

    @GetMapping
    ModelAndView getItems(
        @RequestParam(required = false, name = "source_id") Integer sourceId
    ) {
        return new ViewTemplate("cart")
            .with(
                "cart",
                sourceId == null
                    ? cartService.getUserCartViewForCurrentPrice(getUser())
                    : cartService.getUserCartViewForSpecificPrice(getUser(), sourceId)
            )
            .with("orders", orderService.last(getUser()))
        .modelAndView();
    }

    @Override
    protected HeadMenu.ITEM currentMenuItem() {
        return HeadMenu.ITEM.ORDER;
    }
}
