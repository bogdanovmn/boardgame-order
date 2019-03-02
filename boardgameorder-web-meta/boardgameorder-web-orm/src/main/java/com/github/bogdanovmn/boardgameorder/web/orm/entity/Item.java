package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Setter
@Getter

@Entity
@Table(
	indexes = {
		@Index(
			columnList = "barcode"
		)
	}
)
public class Item extends BaseEntity {
	private final static Pattern BOARD_GAME_PATTERN = Pattern.compile(
		"^.*(наст.*игр|игр.*наст|протекторы).*$",
		Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE
	);
	private final static String EFFECTIVE_TITLE_REGEXP = "\"(.*)\"";
	private final static Pattern EFFECTIVE_TITLE_PATTERN = Pattern.compile(EFFECTIVE_TITLE_REGEXP);

	@Column(nullable = false)
	private String title;
	@Column(length = 20)
	private String barcode;
	private String url;

	@OneToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "publisher_id")
	private Publisher publisher;

	private boolean likeBoardGameTitle = false;
	private String effectiveTitle;

	public String getHtmlTitle() {
		return title.replaceFirst(EFFECTIVE_TITLE_REGEXP, "\"<b>$1</b>\"");
	}

	public Item setTitle(String title) {
		this.title = title;
		likeBoardGameTitle = BOARD_GAME_PATTERN.matcher(title).find();

		Matcher m = EFFECTIVE_TITLE_PATTERN.matcher(title);
		effectiveTitle = m.find()
			? m.group(1).replaceAll("\"", "")
			: title;

		return this;
	}

	@Override
	public String toString() {
		return String.format("Item{title='%s', barcode='%s', publisher=%s, id=%s}", title, barcode, publisher, id);
	}
}
