package com.datarecord.enums;

/**
 * 指标状态：
 * 0：待审批
 * 1：审批通过
 * 2：审批驳回
 * 3：作废
 */
public enum FldConfigStatus {
    REVIEW(0,"待审批"),
    APPROVE(1,"审批通过"),
    REJECT(2,"审批驳回"),
    REMOVE(3,"作废");

    private Integer value;
    private String comment;

    private FldConfigStatus(int value,String comment){
        this.comment=comment;
        this.value=value;
    }

    private FldConfigStatus(int value){
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

    public static synchronized FldConfigStatus getFldConfigStatus(String typeStr){
        return getFldConfigStatus(new Integer(typeStr));
    }

    public static synchronized FldConfigStatus getFldConfigStatus(Integer typeInt){
        FldConfigStatus[] allValues = FldConfigStatus.values();
        for (FldConfigStatus status : allValues) {
            if(status.value.equals(typeInt)){
                return status;
            }
        }
        return null;
    }
}
