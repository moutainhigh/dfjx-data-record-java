package com.datarecord.webapp.process.entity;

public class ReportFldConfig {

    private Integer job_unit_id;
    private String job_unit_name;
    private Integer catg_id;
    private String catg_name;
    private Integer fld_id;
    private String fld_name;
    private String fld_point;
    private String fld_data_type;
    private Integer fld_type;
    private Integer fld_is_null;
    private Integer is_actived;

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
                append("fld_point:").append(fld_point).append("}").
                append("fld_data_type:").append(fld_data_type).append("}").
                append("fld_type:").append(fld_type).append("}").
                append("fld_is_null:").append(fld_is_null).append("}").
                append("is_actived:").append(is_actived).append("}").
                toString();
    }

    public String getFld_point() {
        return fld_point;
    }

    public void setFld_point(String fld_point) {
        this.fld_point = fld_point;
    }

    public String getFld_data_type() {
        return fld_data_type;
    }

    public void setFld_data_type(String fld_data_type) {
        this.fld_data_type = fld_data_type;
    }

    public Integer getFld_type() {
        return fld_type;
    }

    public void setFld_type(Integer fld_type) {
        this.fld_type = fld_type;
    }

    public Integer getFld_is_null() {
        return fld_is_null;
    }

    public void setFld_is_null(Integer fld_is_null) {
        this.fld_is_null = fld_is_null;
    }

    public Integer getIs_actived() {
        return is_actived;
    }

    public void setIs_actived(Integer is_actived) {
        this.is_actived = is_actived;
    }

    public Integer getCatg_id() {
        return catg_id;
    }

    public void setCatg_id(Integer catg_id) {
        this.catg_id = catg_id;
    }

    public String getCatg_name() {
        return catg_name;
    }

    public void setCatg_name(String catg_name) {
        this.catg_name = catg_name;
    }
}
