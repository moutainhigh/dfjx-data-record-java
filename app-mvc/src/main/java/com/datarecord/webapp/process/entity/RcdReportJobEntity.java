package com.datarecord.webapp.process.entity;

public class RcdReportJobEntity {
    private Integer unit_id;
    private Integer colum_id;
    private Integer fld_id;
    private String record_data;
    private Integer record_origin_id;
    private Integer record_user_id;
    private Integer record_status;

    public Integer getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(Integer unit_id) {
        this.unit_id = unit_id;
    }

    public Integer getColum_id() {
        return colum_id;
    }

    public void setColum_id(Integer colum_id) {
        this.colum_id = colum_id;
    }

    public Integer getFld_id() {
        return fld_id;
    }

    public void setFld_id(Integer fld_id) {
        this.fld_id = fld_id;
    }

    public String getRecord_data() {
        return record_data;
    }

    public void setRecord_data(String record_data) {
        this.record_data = record_data;
    }

    public Integer getRecord_origin_id() {
        return record_origin_id;
    }

    public void setRecord_origin_id(Integer record_origin_id) {
        this.record_origin_id = record_origin_id;
    }

    public Integer getRecord_user_id() {
        return record_user_id;
    }

    public void setRecord_user_id(Integer record_user_id) {
        this.record_user_id = record_user_id;
    }

    public Integer getRecord_status() {
        return record_status;
    }

    public void setRecord_status(Integer record_status) {
        this.record_status = record_status;
    }
}
