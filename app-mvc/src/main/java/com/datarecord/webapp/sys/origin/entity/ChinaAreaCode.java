package com.datarecord.webapp.sys.origin.entity;

public class ChinaAreaCode {
    private Integer id;
    private String area_code;
    private String super_area_code;
    private String area_name;
    private String area_level;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getArea_code() {
        return area_code;
    }

    public void setArea_code(String area_code) {
        this.area_code = area_code;
    }

    public String getSuper_area_code() {
        return super_area_code;
    }

    public void setSuper_area_code(String super_area_code) {
        this.super_area_code = super_area_code;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public String getArea_level() {
        return area_level;
    }

    public void setArea_level(String area_level) {
        this.area_level = area_level;
    }
}
