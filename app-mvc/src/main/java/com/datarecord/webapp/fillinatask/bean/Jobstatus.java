package com.datarecord.webapp.fillinatask.bean;

import java.util.Arrays;

public class Jobstatus {

    private String unitId;
    private String[] fldList;


    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String[] getFldList() {
        return fldList;
    }

    public void setFldList(String[] fldList) {
        this.fldList = fldList;
    }

    @Override
    public String toString() {
        return "Jobstatus{" +
                "unitId='" + unitId + '\'' +
                ", fldList=" + Arrays.toString(fldList) +
                '}';
    }
}
