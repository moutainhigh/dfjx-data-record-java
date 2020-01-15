package com.datarecord.webapp.process.entity;

import java.util.Date;
import java.util.List;

public class JobConfig {

    private Integer job_id;
    private String job_name;
    private Integer job_status;
    private Date job_start_dt;
    private Date job_end_dt;
    private List<JobUnitConfig> jobUnits;
    private List<JobPerson> jobPersons;

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public String getJob_name() {
        return job_name;
    }

    public void setJob_name(String job_name) {
        this.job_name = job_name;
    }

    public Integer getJob_status() {
        return job_status;
    }

    public void setJob_status(Integer job_status) {
        this.job_status = job_status;
    }

    public Date getJob_start_dt() {
        return job_start_dt;
    }

    public void setJob_start_dt(Date job_start_dt) {
        this.job_start_dt = job_start_dt;
    }

    public Date getJob_end_dt() {
        return job_end_dt;
    }

    public void setJob_end_dt(Date job_end_dt) {
        this.job_end_dt = job_end_dt;
    }

    public List<JobUnitConfig> getJobUnits() {
        return jobUnits;
    }

    public void setJobUnits(List<JobUnitConfig> jobUnits) {
        this.jobUnits = jobUnits;
    }

    public List<JobPerson> getJobPersons() {
        return jobPersons;
    }

    public void setJobPersons(List<JobPerson> jobPersons) {
        this.jobPersons = jobPersons;
    }

    public String toString(){
        return new StringBuilder().
                append("{job_id:").append(job_id).append(",").
                append("job_name:").append(job_name).append(",").
                append("job_status:").append(job_status).append(",").
                append("job_start_dt:").append(job_start_dt).append(",").
                append("job_end_dt:").append(job_end_dt).append(",").
                append("jobUnits:").append(jobUnits).append("}").
                append("jobPersons:").append(jobPersons).append("}").
                toString();
    }

}
