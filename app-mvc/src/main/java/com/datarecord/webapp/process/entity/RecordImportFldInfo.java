package com.datarecord.webapp.process.entity;

public class RecordImportFldInfo {
    private Integer id;
    private Integer job_id;
    private Integer unit_id;
    private Integer fld_id;
    private Integer fld_order;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public Integer getFld_id() {
        return fld_id;
    }

    public void setFld_id(Integer fld_id) {
        this.fld_id = fld_id;
    }

    public Integer getFld_order() {
        return fld_order;
    }

    public void setFld_order(Integer fld_order) {
        this.fld_order = fld_order;
    }
}
