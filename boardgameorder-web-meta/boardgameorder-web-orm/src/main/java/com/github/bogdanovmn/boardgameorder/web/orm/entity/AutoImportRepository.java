package com.github.bogdanovmn.boardgameorder.web.orm.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface AutoImportRepository extends JpaRepository<AutoImport, Integer> {
    AutoImport findTopByStatusOrderByIdDesc(AutoImportStatus status);

    AutoImport findTopByOrderByIdDesc();

    @Query(
        nativeQuery = true,
        value =
            "select"
                + " file_modify_date fileModifyDate,"
                + " status,"
                + " source_id sourceId,"
                + " max(import_date) lastImportDate,"
                + " error_msg errorMsg,"
                + " count(status) count"
            + " from auto_import"
            + " where status <> 'NO_CHANGES'"
            + " and import_date > now() - interval 2 year"
            + " group by error_msg, file_modify_date, status, source_id"
            + " order by max(import_date) desc;"
    )
    List<AutoImportHistory> getLastHistory();

    interface AutoImportHistory {
        Date getFileModifyDate();
        AutoImportStatus getStatus();
        Integer getSourceId();
        Date getLastImportDate();
        String getErrorMsg();
        Integer getCount();
    }
}
