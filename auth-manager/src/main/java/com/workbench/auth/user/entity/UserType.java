package com.workbench.auth.user.entity;

public enum UserType {
    REPORTER(0,"填报中"),
    //    SUBMIT(1,"审核中"),
    SYSMANAGER(3,"系统管理员");

    private Integer value;
    private String comment;

    private UserType(int value,String comment){
        this.comment=comment;
        this.value=value;
    }

    private UserType(int value){
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

    public static synchronized UserType getUserType(String statusStr){
        return getUserType(new Integer(statusStr));
    }

    public static synchronized UserType getUserType(Integer statusInt){
        UserType[] allValues = UserType.values();
        for (UserType status : allValues) {
            if(status.value.equals(statusInt)){
                return status;
            }
        }
        return null;
    }
}
