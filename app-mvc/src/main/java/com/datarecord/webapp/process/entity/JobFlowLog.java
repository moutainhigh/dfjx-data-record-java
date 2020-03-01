package com.datarecord.webapp.process.entity;

import com.datarecord.enums.JobConfigStatus;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JobFlowLog {
    private Integer id;
    private Integer job_id;
    private String job_name;
    private Integer job_flow_status;
    private String job_flow_status_cn;
    private String job_flow_comment;
    private Integer job_flow_user;
    private String job_flow_user_name;
    private Date job_flow_date;
    private String job_flow_date_str;

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public Integer getJob_flow_status() {
        return job_flow_status;
    }

    public void setJob_flow_status(Integer job_flow_status) {
        this.job_flow_status = job_flow_status;
        if(job_flow_status!=null){
            this.job_flow_status_cn = JobConfigStatus.getJobConfigStatus(job_flow_status).getComment();
        }
    }

    public String getJob_flow_comment() {
        return job_flow_comment;
    }

    public void setJob_flow_comment(String job_flow_comment) {
        this.job_flow_comment = job_flow_comment;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public Integer getJob_flow_user() {
        return job_flow_user;
    }

    public void setJob_flow_user(Integer job_flow_user) {
        this.job_flow_user = job_flow_user;
    }

    public String getJob_flow_user_name() {
        return job_flow_user_name;
    }

    public void setJob_flow_user_name(String job_flow_user_name) {
        this.job_flow_user_name = job_flow_user_name;
    }

    public Date getJob_flow_date() {
        return job_flow_date;
    }

    public void setJob_flow_date(Date job_flow_date) {
        this.job_flow_date = job_flow_date;
        if(job_flow_date!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            this.job_flow_date_str = format.format(job_flow_date);
        }
    }

    public String getJob_flow_date_str() {
        return job_flow_date_str;
    }

    public void setJob_flow_date_str(String job_flow_date_str) {
        this.job_flow_date_str = job_flow_date_str;
    }

    public String getJob_flow_status_cn() {
        return job_flow_status_cn;
    }

    public void setJob_flow_status_cn(String job_flow_status_cn) {
        this.job_flow_status_cn = job_flow_status_cn;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
