package com.datarecord.webapp.reportinggroup.bean;

public class RcdJobUnitFld {

  private int    job_unit_id;
  private int    fld_id;
  private String fld_name;

    public String getFld_name() {
        return fld_name;
    }

    public void setFld_name(String fld_name) {
        this.fld_name = fld_name;
    }

    public int getJob_unit_id() {
        return job_unit_id;
    }

    public void setJob_unit_id(int job_unit_id) {
        this.job_unit_id = job_unit_id;
    }

    public int getFld_id() {
        return fld_id;
    }

    public void setFld_id(int fld_id) {
        this.fld_id = fld_id;
    }
}
