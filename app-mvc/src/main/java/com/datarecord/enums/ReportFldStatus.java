package com.datarecord.enums;

/**
 * 0：填报中
 * 1：已提交
 * 2：未提交
 */
public enum ReportFldStatus {

    NORMAL(0,"填报中"),
//    SUBMIT(1,"审核中"),
    SUBMIT(1,"已提交"),
    UNSUB(2,"未提交");

    private Integer value;
    private String comment;

    private ReportFldStatus(int value, String comment){
        this.comment=comment;
        this.value=value;
    }

    private ReportFldStatus(int value){
        this.value = value;
    }

    public String getValue(){
        return this.value.toString();
    }
    public Integer getValueInteger(){
        return this.value;
    }

    public boolean compareTo(String status){
        if(status.equals(this.value.toString())){
            return true;
        }

        return false;
    }

    public boolean compareTo(int status){
        if(status == this.value){
            return true;
        }

        return false;
    }


    public String getComment() {
        return this.comment;
    }

    public static synchronized ReportFldStatus getReportStatus(String statusStr){
        return getReportStatus(new Integer(statusStr));
    }

    public static synchronized ReportFldStatus getReportStatus(Integer statusInt){
        ReportFldStatus[] allValues = ReportFldStatus.values();
        for (ReportFldStatus status : allValues) {
            if(status.value.equals(statusInt)){
                return status;
            }
        }
        return null;
    }
}
