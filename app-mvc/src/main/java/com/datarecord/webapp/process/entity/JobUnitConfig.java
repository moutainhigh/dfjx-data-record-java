package com.datarecord.webapp.process.entity;

import java.util.List;

public class JobUnitConfig {
    private Integer job_unit_id;
    private String job_unit_name;
    private Integer job_id;
    private Integer job_unit_active;
    private Integer job_unit_type;
    private List<ReportFldConfig> unitFlds;
    private List<ReportFldTypeConfig> unitFldTypes;

    public Integer getJob_unit_id() {
        return job_unit_id;
    }

    public void setJob_unit_id(Integer job_unit_id) {
        this.job_unit_id = job_unit_id;
    }

    public String getJob_unit_name() {
        return job_unit_name;
    }

    public void setJob_unit_name(String job_unit_name) {
        this.job_unit_name = job_unit_name;
    }

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public Integer getJob_unit_active() {
        return job_unit_active;
    }

    public void setJob_unit_active(Integer job_unit_active) {
        this.job_unit_active = job_unit_active;
    }

    public Integer getJob_unit_type() {
        return job_unit_type;
    }

    public void setJob_unit_type(Integer job_unit_type) {
        this.job_unit_type = job_unit_type;
    }

    public List<ReportFldConfig> getUnitFlds() {
        return unitFlds;
    }

    public void setUnitFlds(List<ReportFldConfig> unitFlds) {
        this.unitFlds = unitFlds;
    }


    public String toString(){
        return new StringBuilder().
                append("{job_unit_id:").append(job_unit_id).append(",").
                append("job_unit_name:").append(job_unit_name).append(",").
                append("job_unit_active:").append(job_unit_active).append(",").
                append("job_unit_type:").append(job_unit_type).append(",").
                append("unitFlds:[").append(unitFlds).append("]}").
                toString();
    }

    public List<ReportFldTypeConfig> getUnitFldTypes() {
        return unitFldTypes;
    }

    public void setUnitFldTypes(List<ReportFldTypeConfig> unitFldTypes) {
        this.unitFldTypes = unitFldTypes;
    }
}
