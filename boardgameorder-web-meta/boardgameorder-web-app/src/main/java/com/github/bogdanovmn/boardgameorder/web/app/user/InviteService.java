package com.github.bogdanovmn.boardgameorder.web.app.user;

import com.github.bogdanovmn.boardgameorder.web.orm.Invite;
import com.github.bogdanovmn.boardgameorder.web.orm.InviteRepository;
import com.github.bogdanovmn.boardgameorder.web.orm.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
class InviteService {
	private static final Logger LOG = LoggerFactory.getLogger(InviteService.class);
	@Value("${invite.create-interval-in-seconds}")
	private int createIntervalInSeconds;
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
		Invite lastInvite = inviteRepository.getTopByCreatorOrderByIdDesc(user);
		return
			lastInvite != null
			&&
			lastInvite.getCreateDate().plusSeconds(createIntervalInSeconds).isAfter(LocalDateTime.now());
	}
}
