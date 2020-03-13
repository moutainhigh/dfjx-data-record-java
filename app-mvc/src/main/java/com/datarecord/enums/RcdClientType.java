package com.datarecord.enums;

public enum RcdClientType {

    ALL(0,"所有"),
    MOBILE(1,"移动端"),
    PC(2,"PC端");

    private Integer value;
    private String comment;

    public String getValueStr(){
        return this.value.toString();
    }

    public int getValue(){
        return this.value;
    }

    private RcdClientType(int value,String comment){
        this.comment=comment;
        this.value=value;
    }

    public boolean compareTo(String status){
        if(status.equals(this.value.toString())){
            return true;
        }

        return false;
    }

    public static synchronized RcdClientType getRcdClientType(String typeStr){
        return getFldDataType(new Integer(typeStr));
    }

    public static synchronized RcdClientType getFldDataType(Integer typeInt){
        RcdClientType[] allValues = RcdClientType.values();
        for (RcdClientType status : allValues) {
            if(status.value.equals(typeInt)){
                return status;
            }
        }
        return null;
    }
}
