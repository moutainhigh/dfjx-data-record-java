package com.datarecord.webapp.process.entity;

import java.util.List;

public class ReportJobInfo {
    private Integer report_id;
    private Integer job_id;
    private Integer record_origin_id;
    private Integer record_user_id;
    private Integer record_status;
    private String record_status_cn;
    private List<ReportJobData> reportJobDataList;

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer report_id) {
        this.report_id = report_id;
    }

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public Integer getRecord_origin_id() {
        return record_origin_id;
    }

    public void setRecord_origin_id(Integer record_origin_id) {
        this.record_origin_id = record_origin_id;
    }

    public Integer getRecord_user_id() {
        return record_user_id;
    }

    public void setRecord_user_id(Integer record_user_id) {
        this.record_user_id = record_user_id;
    }

    public Integer getRecord_status() {
        return record_status;
    }

    public void setRecord_status(Integer record_status) {
        this.record_status = record_status;
        if(record_status!=null){
            this.record_status_cn = ReportStatus.getReportStatus(record_status).getComment();
        }
    }

    public List<ReportJobData> getReportJobDataList() {
        return reportJobDataList;
    }

    public void setReportJobDataList(List<ReportJobData> reportJobDataList) {
        this.reportJobDataList = reportJobDataList;
    }

    public String getRecord_status_cn() {
        return record_status_cn;
    }

    public void setRecord_status_cn(String record_status_cn) {
        this.record_status_cn = record_status_cn;
    }
}
