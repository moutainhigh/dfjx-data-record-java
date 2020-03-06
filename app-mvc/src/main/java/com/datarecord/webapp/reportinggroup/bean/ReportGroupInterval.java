package com.datarecord.webapp.reportinggroup.bean;

public class ReportGroupInterval {
    private Integer id;
    private Integer job_id;
    private Integer job_unit_id;
    private String job_unit_start;
    private String job_unit_end;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJob_unit_id() {
        return job_unit_id;
    }

    public void setJob_unit_id(Integer job_unit_id) {
        this.job_unit_id = job_unit_id;
    }

    public String getJob_unit_start() {
        return job_unit_start;
    }

    public void setJob_unit_start(String job_unit_start) {
        this.job_unit_start = job_unit_start;
    }

    public String getJob_unit_end() {
        return job_unit_end;
    }

    public void setJob_unit_end(String job_unit_end) {
        this.job_unit_end = job_unit_end;
    }

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }
}
