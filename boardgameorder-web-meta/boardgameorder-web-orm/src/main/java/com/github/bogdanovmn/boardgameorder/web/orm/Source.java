package com.github.bogdanovmn.boardgameorder.web.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Source extends BaseEntity {
	@Column(nullable = false)
	private Date importDate;
	@Column(nullable = false)
	private Date fileModifyDate;
	@Column(length = 32, unique = true)
	private String contentHash;
}
