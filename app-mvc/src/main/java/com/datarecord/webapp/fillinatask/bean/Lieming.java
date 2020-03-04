package com.datarecord.webapp.fillinatask.bean;

public class Lieming {

  private String    fld_name;
  private String record_data;

  public String getFld_name() {
    return fld_name;
  }

  public void setFld_name(String fld_name) {
    this.fld_name = fld_name;
  }

  public String getRecord_data() {
    return record_data;
  }

  public void setRecord_data(String record_data) {
    this.record_data = record_data;
  }

  @Override
  public String toString() {
    return "Lieming{" +
            "fld_name='" + fld_name + '\'' +
            ", record_data='" + record_data + '\'' +
            '}';
  }
}
