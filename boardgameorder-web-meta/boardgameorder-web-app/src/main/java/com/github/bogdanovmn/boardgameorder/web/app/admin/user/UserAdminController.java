package com.github.bogdanovmn.boardgameorder.web.app.admin.user;

import com.github.bogdanovmn.boardgameorder.web.app.AdminMenu;
import com.github.bogdanovmn.boardgameorder.web.app.admin.AbstractVisualAdminController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
class UserAdminController extends AbstractVisualAdminController {
	private final UserService userService;

	UserAdminController(UserService userService) {
		this.userService = userService;
	}

	@Override
	protected AdminMenu.ITEM currentAdminMenuItem() {
		return AdminMenu.ITEM.USER_LIST;
	}

	@GetMapping("/users")
	ModelAndView users() {
		return new ModelAndView(
			"users_list",
			"users",
			userService.usersSummary()
		);
	}
}
