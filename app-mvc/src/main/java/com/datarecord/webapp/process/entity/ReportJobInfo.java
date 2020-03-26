package com.datarecord.webapp.process.entity;

import com.datarecord.enums.ReportStatus;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReportJobInfo {
    private Integer report_id;
    private Integer job_id;
    private String job_name;
    private BigInteger record_origin_id;
    private String record_origin_name;
    private BigInteger record_user_id;
    private String record_user_name;
    private Integer record_status;
    private String record_status_cn;
    private Date job_start_dt;
    private String job_start_dt_str;
    private Date job_end_dt;
    private String job_end_dt_str;
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

    public BigInteger getRecord_origin_id() {
        return record_origin_id;
    }

    public void setRecord_origin_id(BigInteger record_origin_id) {
        this.record_origin_id = record_origin_id;
    }

    public BigInteger getRecord_user_id() {
        return record_user_id;
    }

    public void setRecord_user_id(BigInteger record_user_id) {
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

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public Date getJob_start_dt() {
        return job_start_dt;
    }

    public void setJob_start_dt(Date job_start_dt) {
        this.job_start_dt = job_start_dt;
        if(job_start_dt!=null){
            this.job_start_dt_str = new SimpleDateFormat("yyyy-MM-dd").format(job_start_dt);
        }
    }

    public Date getJob_end_dt() {
        return job_end_dt;
    }

    public void setJob_end_dt(Date job_end_dt) {
        this.job_end_dt = job_end_dt;
        if(job_end_dt!=null){
            this.job_end_dt_str = new SimpleDateFormat("yyyy-MM-dd").format(job_end_dt);
        }
    }

    public String getJob_start_dt_str() {
        return job_start_dt_str;
    }

    public String getJob_end_dt_str() {
        return job_end_dt_str;
    }

    public String getRecord_user_name() {
        return record_user_name;
    }

    public void setRecord_user_name(String record_user_name) {
        this.record_user_name = record_user_name;
    }

    public String getRecord_origin_name() {
        return record_origin_name;
    }

    public void setRecord_origin_name(String record_origin_name) {
        this.record_origin_name = record_origin_name;
    }
}
