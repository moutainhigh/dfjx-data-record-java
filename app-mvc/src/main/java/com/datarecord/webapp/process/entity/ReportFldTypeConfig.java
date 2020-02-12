package com.datarecord.webapp.process.entity;

import java.util.List;

public class ReportFldTypeConfig {
    private int catg_id;
    private String catg_name;
    private int  proj_id;
    private String proj_name;
    private List<ReportFldConfig> unitFlds;

    public int getProj_id() {
        return proj_id;
    }

    public void setProj_id(int proj_id) {
        this.proj_id = proj_id;
    }

    public String getProj_name() {
        return proj_name;
    }

    public void setProj_name(String proj_name) {
        this.proj_name = proj_name;
    }

    public int getCatg_id() {
        return catg_id;
    }

    public void setCatg_id(int catg_id) {
        this.catg_id = catg_id;
    }

    public String getCatg_name() {
        return catg_name;
    }

    public void setCatg_name(String catg_name) {
        this.catg_name = catg_name;
    }

    public List<ReportFldConfig> getUnitFlds() {
        return unitFlds;
    }

    public void setUnitFlds(List<ReportFldConfig> unitFlds) {
        this.unitFlds = unitFlds;
    }
}
