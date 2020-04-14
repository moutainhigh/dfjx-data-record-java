package com.datarecord.webapp.process.dao;

import com.datarecord.enums.ReportFileLogStatus;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportFileLog {
    private Integer log_id;
    private Integer report_id;
    private Integer job_id;
    private Date start_time;
    private String start_time_str;
    private Date end_time;
    private String end_time_str;
    private Integer log_status;
    private String log_status_str;
    private BigInteger log_user;
    private String log_user_name;
    private String comment;
    private String file_path;

    public Integer getLog_id() {
        return log_id;
    }

    public void setLog_id(Integer log_id) {
        this.log_id = log_id;
    }

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

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
        if(start_time!=null){
            this.start_time_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(start_time);
        }
    }

    public Date getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Date end_time) {
        this.end_time = end_time;
        if(end_time!=null){
            this.end_time_str = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(end_time);
        }
    }

    public Integer getLog_status() {
        return log_status;
    }

    public void setLog_status(Integer log_status) {
        this.log_status = log_status;
        if(log_status!=null){
            this.log_status_str = ReportFileLogStatus.getStatus(log_status).getComment();
        }
    }

    public BigInteger getLog_user() {
        return log_user;
    }

    public void setLog_user(BigInteger log_user) {
        this.log_user = log_user;
    }

    public String getLog_user_name() {
        return log_user_name;
    }

    public void setLog_user_name(String log_user_name) {
        this.log_user_name = log_user_name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getStart_time_str() {
        return start_time_str;
    }

    public void setStart_time_str(String start_time_str) {
        this.start_time_str = start_time_str;
    }

    public String getEnd_time_str() {
        return end_time_str;
    }

    public void setEnd_time_str(String end_time_str) {
        this.end_time_str = end_time_str;
    }

    public String getLog_status_str() {
        return log_status_str;
    }

    public void setLog_status_str(String log_status_str) {
        this.log_status_str = log_status_str;
    }
}
