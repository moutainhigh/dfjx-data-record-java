package com.datarecord.webapp.process.entity;

/**
 * 0：正常
 * 1：审批中
 * 2：复核中
 * 3：锁定
 * 4：失效
 * 5：报表发布
 * 6：待上传签名
 * 7：过期
 */
public enum ReportStatus {

    NORMAL(0,"填报中"),
    SUBMIT(1,"审批中"),
    REVIEW(2,"复核中"),
    LOCK(3,"锁定"),
    REMOVE(4,"失效"),
    APPROVE(5,"报表发布"),
    UP_SIGIN(6,"待上传签名"),
    TOO_EARLY(7,"未到填写日期"),
    OVER_TIME(8,"过期"),
    REPORT_DONE(9,"填报完成");

    private Integer value;
    private String comment;

    private ReportStatus(int value,String comment){
        this.comment=comment;
        this.value=value;
    }

    private ReportStatus(int value){
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

    public static synchronized ReportStatus getReportStatus(String statusStr){
        return getReportStatus(new Integer(statusStr));
    }

    public static synchronized ReportStatus getReportStatus(Integer statusInt){
        ReportStatus[] allValues = ReportStatus.values();
        for (ReportStatus status : allValues) {
            if(status.value.equals(statusInt)){
                return status;
            }
        }
        return null;
    }
}
