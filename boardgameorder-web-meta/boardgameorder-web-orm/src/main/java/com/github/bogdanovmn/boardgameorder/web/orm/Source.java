package com.github.bogdanovmn.boardgameorder.web.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@Entity
public class Source extends BaseEntity {
	@Column(nullable = false)
	private Date importDate;
	@Column(nullable = false)
	private Date fileCreateDate;
	@Column(nullable = false)
	private Date fileModifyDate;
	@Column(length = 32, unique = true)
	private String contentHash;
	private Integer itemsCount;

	public Date getImportDate() {
		return importDate;
	}

	public Source setImportDate(Date importDate) {
		this.importDate = importDate;
		return this;
	}

	public Date getFileModifyDate() {
		return fileModifyDate;
	}

	public Source setFileModifyDate(Date fileModifyDate) {
		this.fileModifyDate = fileModifyDate;
		return this;
	}

	public String getContentHash() {
		return contentHash;
	}

	public Source setContentHash(String contentHash) {
		this.contentHash = contentHash;
		return this;
	}

	public Date getFileCreateDate() {
		return fileCreateDate;
	}

	public Source setFileCreateDate(final Date fileCreateDate) {
		this.fileCreateDate = fileCreateDate;
		return this;
	}

	public Integer getItemsCount() {
		return itemsCount;
	}

	public Source setItemsCount(final Integer itemsCount) {
		this.itemsCount = itemsCount;
		return this;
	}

	@Override
	public String toString() {
		return String.format(
			"Source [ lastModified: %s; itemsCount: %d ]",
				fileModifyDate.toString(), itemsCount
		);
	}
}
