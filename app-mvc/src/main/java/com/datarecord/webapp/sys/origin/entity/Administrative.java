package com.datarecord.webapp.sys.origin.entity;

import java.math.BigInteger;

/**
 * 表organizations
 */
public class Administrative {

    private BigInteger origin_id;
    private String origin_name;
    private Integer organization_id;
    private String organization_name;
    private String create_time;
    private BigInteger create_user;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    private String user_name;

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

    public Integer getOrganization_id() {
        return organization_id;
    }

    public void setOrganization_id(Integer organization_id) {
        this.organization_id = organization_id;
    }

    public String getOrganization_name() {
        return organization_name;
    }

    public void setOrganization_name(String organization_name) {
        this.organization_name = organization_name;
    }


    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public BigInteger getCreate_user() {
        return create_user;
    }

    public void setCreate_user(BigInteger create_user) {
        this.create_user = create_user;
    }
}
