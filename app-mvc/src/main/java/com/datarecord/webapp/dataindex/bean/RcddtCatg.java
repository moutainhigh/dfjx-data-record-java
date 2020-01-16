package com.datarecord.webapp.dataindex.bean;

public class RcddtCatg {

    private int catg_id;
    private String catg_name;
    private int  fld_id;
    private String fld_name;
    private int  proj_id;
    private String proj_name;

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

    public int getFld_id() {
        return fld_id;
    }

    public void setFld_id(int fld_id) {
        this.fld_id = fld_id;
    }

    public String getFld_name() {
        return fld_name;
    }

    public void setFld_name(String fld_name) {
        this.fld_name = fld_name;
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
}
