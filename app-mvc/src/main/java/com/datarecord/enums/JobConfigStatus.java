package com.datarecord.enums;

/**
 * 0:编辑中
 * 1:失效
 * 2:锁定
 * 3:软删除
 * 4:已发布
 * 5:发布中
 * 6:待审核
 * 7:审核通过
 * 8:审核驳回
 */
public enum JobConfigStatus {

    NORMAL(0,"编辑中"),
    FAIL(1,"失效"),
    LOCK(2,"锁定"),
    DELETE(3,"已删除"),
    SUBMITING(5,"发布中"),
    REVIEW(6,"待审核"),
    APPROVE(7,"审核通过"),
    REJECT(8,"审核驳回"),
    SUBMIT(4,"已发布");

    private Integer value;
    private String comment;

    private JobConfigStatus(int value,String comment){
        this.comment=comment;
        this.value=value;
    }
    public String toString(){
        return this.value.toString();
    }
    public boolean compareWith(Integer status){
        boolean compareResult = false;
        if(status!=null&&this.value.equals(status) ){
            return true;
        }
        return false;
    }

    public int value(){
        return this.value;
    }

    public static synchronized JobConfigStatus getJobConfigStatus(String statusStr){
        return getJobConfigStatus(new Integer(statusStr));
    }

    public static synchronized JobConfigStatus getJobConfigStatus(Integer typeInt){
        JobConfigStatus[] allValues = JobConfigStatus.values();
        for (JobConfigStatus status : allValues) {
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
