package com.github.bogdanovmn.boardgameorder.web.orm;

import org.springframework.util.DigestUtils;

import javax.persistence.*;
import java.time.Instant;

@Entity
public class Invite extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_user_id")
	private User creator;

	@Column(unique = true)
	private String code;

	@Column(nullable = false)
	private Instant createDate = Instant.now();

	@Column(nullable = false)
	private Instant expireDate = Instant.now().plusSeconds(3 * 86400);

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "invited_user_id")
	private User invited;

	public User getCreator() {
		return creator;
	}

	public Invite setCreator(User creator) {
		this.creator = creator;
		this.code = DigestUtils.md5DigestAsHex(
			(creator.id.toString() + createDate.toString()).getBytes()
		);
		return this;
	}

	public User getInvited() {
		return invited;
	}

	public Invite setInvited(User invited) {
		this.invited = invited;
		return this;
	}

	public Instant getCreateDate() {
		return createDate;
	}

	public String getCode() {
		return code;
	}
}
