package com.datarecord.webapp.process.entity;

public enum JobUnitType {

    GRID(0),
    SIMPLE(1);

    private Integer value;

    private JobUnitType(int value){
        this.value = value;
    }

    public int value(){
        return this.value;
    }

}
