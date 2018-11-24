package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import org.springframework.util.DigestUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Invite extends BaseEntity {
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "creator_user_id")
	private User creator;

	@Column(unique = true, length = 32)
	private String code;

	@Column(nullable = false)
	private LocalDateTime createDate = LocalDateTime.now();

	@Column(nullable = false)
	private LocalDateTime expireDate = LocalDateTime.now().plusSeconds(3 * 86400);

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

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public String getCode() {
		return code;
	}

	public LocalDateTime getExpireDate() {
		return expireDate;
	}
}
