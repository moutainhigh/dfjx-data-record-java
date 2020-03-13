package com.datarecord.webapp.process.entity;

import com.datarecord.enums.JobConfigStatus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class JobConfig {

    private Integer job_id;
    private String job_name;
    private Integer job_status;
    private String job_status_str;
    private Date job_start_dt;
    private String job_start_dt_str;
    private Date job_end_dt;
    private String job_end_dt_str;
    private Integer job_creater;
    private String job_creater_name;
    private Integer job_creater_origin;
    private String job_creater_origin_name;
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
        if(job_status!=null){
            this.job_status_str = JobConfigStatus.getJobConfigStatus(job_status).getComment();
        }
    }

    public Date getJob_start_dt() {
        return job_start_dt;
    }

    public void setJob_start_dt(Date job_start_dt) {
        this.job_start_dt = job_start_dt;
        if(job_start_dt!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            this.job_start_dt_str = format.format(job_start_dt);
        }
    }

    public Date getJob_end_dt() {
        return job_end_dt;
    }

    public void setJob_end_dt(Date job_end_dt) {
        this.job_end_dt = job_end_dt;
        if(job_end_dt!=null){
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            this.job_end_dt_str = format.format(job_end_dt);
        }
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

    public Integer getJob_creater() {
        return job_creater;
    }

    public void setJob_creater(Integer job_creater) {
        this.job_creater = job_creater;
    }

    public String getJob_creater_name() {
        return job_creater_name;
    }

    public void setJob_creater_name(String job_creater_name) {
        this.job_creater_name = job_creater_name;
    }

    public Integer getJob_creater_origin() {
        return job_creater_origin;
    }

    public void setJob_creater_origin(Integer job_creater_origin) {
        this.job_creater_origin = job_creater_origin;
    }

    public String getJob_creater_origin_name() {
        return job_creater_origin_name;
    }

    public void setJob_creater_origin_name(String job_creater_origin_name) {
        this.job_creater_origin_name = job_creater_origin_name;
    }

    public String getJob_start_dt_str() {
        return job_start_dt_str;
    }

    public void setJob_start_dt_str(String job_start_dt_str) {
        this.job_start_dt_str = job_start_dt_str;
    }

    public String getJob_end_dt_str() {
        return job_end_dt_str;
    }

    public void setJob_end_dt_str(String job_end_dt_str) {
        this.job_end_dt_str = job_end_dt_str;
    }

    public String getJob_status_str() {
        return job_status_str;
    }

    public void setJob_status_str(String job_status_str) {
        this.job_status_str = job_status_str;
    }
}
