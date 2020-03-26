package com.datarecord.webapp.fillinatask.bean;

import java.math.BigInteger;

public class RcdJobPersonAssign {
    private BigInteger user_id;
    private BigInteger  origin_id;
    private String user_name_cn;

    public String getUser_name_cn() {
        return user_name_cn;
    }

    public void setUser_name_cn(String user_name_cn) {
        this.user_name_cn = user_name_cn;
    }

    public BigInteger getUser_id() {
        return user_id;
    }

    public void setUser_id(BigInteger user_id) {
        this.user_id = user_id;
    }

    public BigInteger getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(BigInteger origin_id) {
        this.origin_id = origin_id;
    }

    @Override
    public String toString() {
        return "RcdJobPersonAssign{" +
                "user_id=" + user_id +
                ", origin_id=" + origin_id +
                ", user_name_cn='" + user_name_cn + '\'' +
                '}';
    }
}
