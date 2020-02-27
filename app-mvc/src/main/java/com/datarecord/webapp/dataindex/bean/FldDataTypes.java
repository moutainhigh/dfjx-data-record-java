package com.datarecord.webapp.dataindex.bean;

public enum  FldDataTypes {

    STRING(0,"字符串"),
    NUMBER(1,"数字"),
    DATE(2,"日期"),
    DICT(3,"");

    private Integer value;
    private String comment;

    private FldDataTypes(int value,String comment){
        this.comment=comment;
        this.value=value;
    }

    private FldDataTypes(int value){
        this.value = value;
    }

    public String getValue(){
        return this.value.toString();
    }

    public boolean compareTo(String status){
        if(status.equals(this.value.toString())){
            return true;
        }

        return false;
    }

    public boolean compareTo(int type){
        if(type == this.value){
            return true;
        }

        return false;
    }


    public String getComment() {
        return this.comment;
    }

    public static synchronized FldDataTypes getFldDataType(String typeStr){
        return getFldDataType(new Integer(typeStr));
    }

    public static synchronized FldDataTypes getFldDataType(Integer typeInt){
        FldDataTypes[] allValues = FldDataTypes.values();
        for (FldDataTypes status : allValues) {
            if(status.value.equals(typeInt)){
                return status;
            }
        }
        return null;
    }

}
