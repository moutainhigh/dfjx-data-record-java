package com.datarecord.enums;

public enum ReportFileLogStatus {
    CREATING(0,"生成中"),
    DONE(1,"成功"),
    ERROR(2,"失败");

    private Integer value;
    private String comment;

    ReportFileLogStatus(int value,String comment){
        this.comment=comment;
        this.value=value;
    }

    public Integer getValue(){
        return this.value;
    }

    public static synchronized ReportFileLogStatus getStatus(String statusStr){
        return getStatus(new Integer(statusStr));
    }

    public static synchronized ReportFileLogStatus getStatus(Integer typeInt){
        ReportFileLogStatus[] allValues = ReportFileLogStatus.values();
        for (ReportFileLogStatus status : allValues) {
            if(status.value.equals(typeInt)){
                return status;
            }
        }
        return null;
    }

    public String getComment() {
        return this.comment;
    }
}
