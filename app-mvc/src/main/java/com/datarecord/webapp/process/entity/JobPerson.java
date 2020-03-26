package com.datarecord.webapp.process.entity;

import java.math.BigInteger;

public class JobPerson {
    private Integer job_id ;
    private BigInteger user_id;
    private String user_name;
    private BigInteger origin_id;
    private String origin_name;

    public Integer getJob_id() {
        return job_id;
    }

    public void setJob_id(Integer job_id) {
        this.job_id = job_id;
    }

    public BigInteger getUser_id() {
        return user_id;
    }

    public void setUser_id(BigInteger user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public BigInteger getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(BigInteger origin_id) {
        this.origin_id = origin_id;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }
}
