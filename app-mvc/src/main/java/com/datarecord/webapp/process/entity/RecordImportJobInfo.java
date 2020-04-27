package com.datarecord.webapp.process.entity;

import java.util.Date;
import java.util.List;

public class RecordImportJobInfo {

    private Integer job_id;
    private String template_file_path;
    private Date template_create_date;
    private List<RecordImportUnitInfo> recordImportUnitInfoList;

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public String getTemplate_file_path() {
        return template_file_path;
    }

    public void setTemplate_file_path(String template_file_path) {
        this.template_file_path = template_file_path;
    }

    public Date getTemplate_create_date() {
        return template_create_date;
    }

    public void setTemplate_create_date(Date template_create_date) {
        this.template_create_date = template_create_date;
    }

    public List<RecordImportUnitInfo> getRecordImportUnitInfoList() {
        return recordImportUnitInfoList;
    }

    public void setRecordImportUnitInfoList(List<RecordImportUnitInfo> recordImportUnitInfoList) {
        this.recordImportUnitInfoList = recordImportUnitInfoList;
    }
}
