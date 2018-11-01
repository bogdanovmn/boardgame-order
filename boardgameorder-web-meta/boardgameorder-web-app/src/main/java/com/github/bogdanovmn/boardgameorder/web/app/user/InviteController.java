package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.app.AbstractVisualController;
import com.github.bogdanovmn.boardgameorder.web.app.HeadMenu;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/invites")
class InviteController extends AbstractVisualController {
	private final InviteService inviteService;

	InviteController(InviteService inviteService) {
		this.inviteService = inviteService;
	}

	@GetMapping
	ModelAndView allActive() {
		return new ModelAndView(
			"invites_active_list",
			"invites",
			inviteService.allActiveByUser(getUser())
		);
	}

	@PostMapping
	String create() {
		inviteService.create(getUser());
		return "redirect:/invites";
	}

	@Override
	protected HeadMenu.ITEM currentMenuItem() {
		return HeadMenu.ITEM.INVITE;
	}
}