package com.datarecord.webapp.process.entity;

import java.util.Date;

public class JobPersonGroupLog {
    private Integer job_id;
    private Integer group_id;
    private String group_name;
    private Date job_make_date;

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public Date getJob_make_date() {
        return job_make_date;
    }

    public void setJob_make_date(Date job_make_date) {
        this.job_make_date = job_make_date;
    }
}
