package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.orm.entity.Invite;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.InviteRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class InviteService {
	private static final Logger LOG = LoggerFactory.getLogger(InviteService.class);
	@Value("${invite.max-active}")
	private int maxActive;
	private final InviteRepository inviteRepository;

	InviteService(InviteRepository inviteRepository) {
		this.inviteRepository = inviteRepository;
	}

	UserInvites userInvites(User user) {
		return UserInvites.from(
			inviteRepository.getAllByCreator(user)
		);
	}

	void create(User user) {
		if (isCreateInviteLimitReached(user)) {
			LOG.info("Wait some time for new invite");
		}
		else {
			inviteRepository.save(
				new Invite().setCreator(user)
			);
		}
	}

	Invite findByCode(String code) {
		return inviteRepository.findFirstByCode(code);
	}

	private boolean isCreateInviteLimitReached(User user) {
		UserInvites invites = UserInvites.from(
			inviteRepository.getAllByCreator(user)
		);
		return invites.activeCount() > maxActive;
	}
}
