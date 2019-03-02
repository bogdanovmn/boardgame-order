package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter

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
}
