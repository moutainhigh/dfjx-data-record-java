package com.datarecord.webapp.process.entity;

public class JobUnitFldConfig {

    private Integer job_unit_id;
    private String job_unit_name;
    private Integer fld_id;
    private String fld_name;

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

    public Integer getFld_id() {
        return fld_id;
    }

    public void setFld_id(Integer fld_id) {
        this.fld_id = fld_id;
    }

    public String getFld_name() {
        return fld_name;
    }

    public void setFld_name(String fld_name) {
        this.fld_name = fld_name;
    }


    public String toString(){
        return new StringBuilder().
                append("{fld_id:").append(fld_id).append(",").
                append("fld_name:").append(fld_name).append("}").
                toString();
    }
}
