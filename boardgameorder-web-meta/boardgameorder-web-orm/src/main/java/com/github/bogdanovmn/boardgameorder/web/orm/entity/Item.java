package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
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
    private static final Pattern BOARD_GAME_PATTERN = Pattern.compile(
        "^.*(наст.*игр|игр.*наст|протекторы).*$",
        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE
    );
    private static final String EFFECTIVE_TITLE_REGEXP = "\"(.*)\"";
    private static final Pattern EFFECTIVE_TITLE_PATTERN = Pattern.compile(EFFECTIVE_TITLE_REGEXP);
    private static final Pattern FIX_PRICE_PATTERN = Pattern.compile(
        "^.*(фикс[.а-я]*\\s*цена).*$",
        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE
    );

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

    public boolean isLikeFixPriceTitle() {
        return FIX_PRICE_PATTERN.matcher(title).find();
    }

    @Override
    public String toString() {
        return String.format("Item{title='%s', barcode='%s', publisher=%s, id=%s}", title, barcode, publisher, id);
    }
}
