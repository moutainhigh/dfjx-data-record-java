package com.datarecord.webapp.reportinggroup.bean;

import com.datarecord.enums.JobConfigStatus;

public class rcdJobConfig {

     private  int        job_id;
     private String      job_name;
     private int  job_status;
     private String  job_status_cn;

    public int getJob_status() {
        return job_status;
    }

    public void setJob_status(int job_status) {
        this.job_status = job_status;
        this.job_status_cn = JobConfigStatus.getJobConfigStatus(job_status).getComment();
    }

    public int getJob_id() {
        return job_id;
    }

    public void setJob_id(int job_id) {
        this.job_id = job_id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public String getJob_status_cn() {
        return job_status_cn;
    }

    public void setJob_status_cn(String job_status_cn) {
        this.job_status_cn = job_status_cn;
    }
}
