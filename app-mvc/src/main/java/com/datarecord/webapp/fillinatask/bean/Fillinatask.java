package com.datarecord.webapp.fillinatask.bean;

public class Fillinatask {
    private int  job_id;
    private String job_name;
    private int job_status;
    private String job_start_dt;
    private String job_end_dt;

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

    public int getJob_status() {
        return job_status;
    }

    public void setJob_status(int job_status) {
        this.job_status = job_status;
    }

    public String getJob_start_dt() {
        return job_start_dt;
    }

    public void setJob_start_dt(String job_start_dt) {
        this.job_start_dt = job_start_dt;
    }

    public String getJob_end_dt() {
        return job_end_dt;
    }

    public void setJob_end_dt(String job_end_dt) {
        this.job_end_dt = job_end_dt;
    }

    @Override
    public String
    toString() {
        return "Fillinatask{" +
                "job_id=" + job_id +
                ", job_name='" + job_name + '\'' +
                ", job_status=" + job_status +
                ", job_start_dt='" + job_start_dt + '\'' +
                ", job_end_dt='" + job_end_dt + '\'' +
                '}';
    }
}
