package com.datarecord.webapp.fillinatask.bean;

public enum  JobUnitAcitve {

    ACTIVE(1),
    UNACTIVE(0);

    private Integer value;

    private JobUnitAcitve(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }

}
