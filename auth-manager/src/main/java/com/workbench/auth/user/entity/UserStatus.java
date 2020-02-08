package com.workbench.auth.user.entity;

public enum UserStatus {

    NORMAL(0,"正常"),
    LOCK(1,"锁定"),
    DELETE(2,"软删除"),
    PWD_EXPIRED(3,"密码过期"),
    STOP(4,"停用"),
    NEVER_LOGIN(5,"从未登陆"),
    NOT_NOMAL_TAG(6,"非正常标记");//仅将该用户打为不正常,不影响任何动作

    private int status ;

    private String status_cn;


    UserStatus(int status,String status_cn){
        this.status = status;
        this.status_cn = status_cn;
    }

    public int getStatus(){
        return status;
    }

    public String getStatus_cn(){
        return status_cn;
    }

    public boolean equal(int status){
        if(this.status == status)
            return true;
        else
            return false;
    }

}
