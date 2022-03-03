package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter

@Entity
public class Source extends BaseEntity {
    @Column(nullable = false)
    private Date importDate;
    @Column(nullable = false)
    private Date fileModifyDate;
    @Column(length = 32, unique = true)
    private String contentHash;
    private Integer itemsCount;
    @Enumerated(EnumType.STRING)
    private ImportType importType;

    public String getFileModifyDateFormatted() {
        return new SimpleDateFormat("yyyy-MM-dd").format(fileModifyDate);
    }

    public String getFileModifyTimeFormatted() {
        return new SimpleDateFormat("HH:mm:ss").format(fileModifyDate);
    }

    @Override
    public String toString() {
        return String.format("Source{fileModifyDate=%s, itemsCount=%d, id=%d}", fileModifyDate, itemsCount, id);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Source source = (Source) o;
        return Objects.equals(contentHash, source.contentHash)
            && Objects.equals(itemsCount, source.itemsCount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(contentHash, itemsCount);
    }
}
