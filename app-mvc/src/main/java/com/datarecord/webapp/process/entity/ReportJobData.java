package com.datarecord.webapp.process.entity;


public class ReportJobData {

    private Integer id;
    private Integer report_id ;
    private Integer unit_id;
    private Integer colum_id;
    private Integer fld_id;
    private String record_data;
    private Integer data_status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer report_id) {
        this.report_id = report_id;
    }

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

    public Integer getData_status() {
        return data_status;
    }

    public void setData_status(Integer data_status) {
        this.data_status = data_status;
    }
}
