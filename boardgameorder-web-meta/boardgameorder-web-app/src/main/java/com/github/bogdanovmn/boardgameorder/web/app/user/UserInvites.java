package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.orm.Invite;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

class UserInvites {
	private final List<Invite> active;
	private final List<Invite> completed;
	private final int expired;

	private UserInvites(List<Invite> active, List<Invite> completed, int expired) {
		this.active = active;
		this.completed = completed;
		this.expired = expired;
	}

	static UserInvites from(List<Invite> allInvites) {
		List<Invite> active = new LinkedList<>();
		List<Invite> completed = new LinkedList<>();
		int expired = 0;

		for (Invite inv : allInvites) {
			if (inv.getInvited() != null) {
				completed.add(inv);
			}
			else if (inv.getExpireDate().isAfter(LocalDateTime.now())) {
				active.add(inv);
			}
			else {
				expired++;
			}
		}

		return new UserInvites(active, completed, expired);
	}
}
