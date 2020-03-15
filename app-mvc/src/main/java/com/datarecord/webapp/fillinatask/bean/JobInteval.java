package com.datarecord.webapp.fillinatask.bean;

public class JobInteval {

    private Integer job_id;
    private String job_interval_start;
    private String job_interval_end;

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public String getJob_interval_start() {
        return job_interval_start;
    }

    public void setJob_interval_start(String job_interval_start) {
        this.job_interval_start = job_interval_start;
    }

    public String getJob_interval_end() {
        return job_interval_end;
    }

    public void setJob_interval_end(String job_interval_end) {
        this.job_interval_end = job_interval_end;
    }
}
