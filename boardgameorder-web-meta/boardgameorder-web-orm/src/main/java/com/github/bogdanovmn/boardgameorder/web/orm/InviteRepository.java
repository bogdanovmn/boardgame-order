package com.github.bogdanovmn.boardgameorder.web.orm;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface InviteRepository extends JpaRepository<Invite, Integer> {
	List<Invite> getAllByCreator(User creator);

	Invite getTopByCreatorOrderByIdDesc(User creator);

	Invite findFirstByCode(String code);

	List<Invite> getAllByCreatorAndExpireDateAfterOrInvitedNotNull(User creator, LocalDateTime now);
}
