package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import com.github.bogdanovmn.common.spring.jpa.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import java.util.Date;

@Setter
@Getter

@Entity
public class AutoImport extends BaseEntity {
    @Column(nullable = false)
    private Date importDate;
    private Date fileModifyDate;
    @Enumerated(EnumType.STRING)
    private AutoImportStatus status;
    private String errorMsg;
    @OneToOne
    private Source source;

    public AutoImport setErrorMsg(final String errorMsg) {
        this.errorMsg = errorMsg;
        if (errorMsg != null) {
            status = AutoImportStatus.ERROR;
        }
        return this;
    }
}
