package com.github.bogdanovmn.boardgameorder.web.orm;

import javax.persistence.*;
import java.util.Date;

@Entity
public class AutoImport extends BaseEntity {
	@Column(nullable = false)
	private Date importDate;
	private Date fileModifyDate;
	@Enumerated(EnumType.STRING)
	private AutoImportStatus status;
	private String errorMsg;
	@OneToOne
//	@JoinColumn(name = "source_id")
	private Source source;


	public Source getSource() {
		return source;
	}

	public AutoImport setSource(final Source source) {
		this.source = source;
		return this;
	}

	public Date getImportDate() {
		return importDate;
	}

	public AutoImport setImportDate(final Date importDate) {
		this.importDate = importDate;
		return this;
	}

	public Date getFileModifyDate() {
		return fileModifyDate;
	}

	public AutoImport setFileModifyDate(final Date fileModifyDate) {
		this.fileModifyDate = fileModifyDate;
		return this;
	}

	public AutoImportStatus getStatus() {
		return status;
	}

	public AutoImport setStatus(final AutoImportStatus status) {
		this.status = status;
		return this;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public AutoImport setErrorMsg(final String errorMsg) {
		this.errorMsg = errorMsg;
		if (errorMsg != null) {
			status = AutoImportStatus.ERROR;
		}
		return this;
	}
}
