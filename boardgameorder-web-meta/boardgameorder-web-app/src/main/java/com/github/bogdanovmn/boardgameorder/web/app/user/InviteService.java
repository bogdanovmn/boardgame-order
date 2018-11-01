package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.orm.Invite;
import com.github.bogdanovmn.boardgameorder.web.orm.InviteRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
class InviteService {
	private static final Logger LOG = LoggerFactory.getLogger(InviteService.class);

	private final InviteRepository inviteRepository;

	InviteService(InviteRepository inviteRepository) {
		this.inviteRepository = inviteRepository;
	}

	List<Invite> allActiveByUser(User user) {
		return inviteRepository.getAllByCreator(user);
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
		Invite lastInvite = inviteRepository.getTopByCreatorOrderById(user);
		return
			lastInvite != null
			&&
			lastInvite.getCreateDate().plusSeconds(1800).isBefore(Instant.now());
	}

	void complete(Invite invite, User user) {
		inviteRepository.save(
			invite.setInvited(user)
		);
	}
}
