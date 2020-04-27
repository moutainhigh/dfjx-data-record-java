package com.datarecord.webapp.process.entity;

import java.util.List;

public class RecordImportUnitInfo {
    private Integer id;
    private Integer job_id;
    private Integer unit_id;
    private Integer unit_order;
    private List<RecordImportFldInfo> recordImportFldInfoList;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public Integer getUnit_order() {
        return unit_order;
    }

    public void setUnit_order(Integer unit_order) {
        this.unit_order = unit_order;
    }

    public List<RecordImportFldInfo> getRecordImportFldInfoList() {
        return recordImportFldInfoList;
    }

    public void setRecordImportFldInfoList(List<RecordImportFldInfo> recordImportFldInfoList) {
        this.recordImportFldInfoList = recordImportFldInfoList;
    }
}
