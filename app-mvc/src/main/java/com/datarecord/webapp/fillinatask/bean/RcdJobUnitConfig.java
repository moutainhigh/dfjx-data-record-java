package com.datarecord.webapp.fillinatask.bean;

public class RcdJobUnitConfig {

    private int job_unit_id;
    private String job_unit_name;


    public int getJob_unit_id() {
        return job_unit_id;
    }

    public void setJob_unit_id(int job_unit_id) {
        this.job_unit_id = job_unit_id;
    }

    public String getJob_unit_name() {
        return job_unit_name;
    }

    public void setJob_unit_name(String job_unit_name) {
        this.job_unit_name = job_unit_name;
    }

    @Override
    public String toString() {
        return "RcdJobUnitConfig{" +
                "job_unit_id=" + job_unit_id +
                ", job_unit_name='" + job_unit_name + '\'' +
                '}';
    }
}
