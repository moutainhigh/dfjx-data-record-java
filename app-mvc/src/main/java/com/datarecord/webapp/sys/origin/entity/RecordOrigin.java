package com.datarecord.webapp.sys.origin.entity;

import java.util.Date;

/**
 * 表sys_origin
 */
public class RecordOrigin {

    private Integer origin_id;
    private String origin_name;
    private Integer parent_origin_id;
    private String origin_status;
    private String origin_type;
    private Date create_date;
    private String create_user;


    public Integer getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(Integer origin_id) {
        this.origin_id = origin_id;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public Integer getParent_origin_id() {
        return parent_origin_id;
    }

    public void setParent_origin_id(Integer parent_origin_id) {
        this.parent_origin_id = parent_origin_id;
    }

    public String getOrigin_status() {
        return origin_status;
    }

    public void setOrigin_status(String origin_status) {
        this.origin_status = origin_status;
    }

    public Date getCreate_date() {
        return create_date;
    }

    public void setCreate_date(Date create_date) {
        this.create_date = create_date;
    }

    public String getCreate_user() {
        return create_user;
    }

    public void setCreate_user(String create_user) {
        this.create_user = create_user;
    }

    public String getOrigin_type() {
        return origin_type;
    }

    public void setOrigin_type(String origin_type) {
        this.origin_type = origin_type;
    }
}
