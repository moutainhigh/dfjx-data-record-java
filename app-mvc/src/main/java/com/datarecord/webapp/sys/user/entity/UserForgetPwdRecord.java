package com.datarecord.webapp.sys.user.entity;

import java.util.Date;

public class UserForgetPwdRecord {
    private Integer id;
    private Integer user_id;
    private String validate_code;
    private String sms_validate_code;
    private Date validate_code_time;
    private Date sms_validate_code_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getValidate_code() {
        return validate_code;
    }

    public void setValidate_code(String validate_code) {
        this.validate_code = validate_code;
    }

    public String getSms_validate_code() {
        return sms_validate_code;
    }

    public void setSms_validate_code(String sms_validate_code) {
        this.sms_validate_code = sms_validate_code;
    }

    public Date getValidate_code_time() {
        return validate_code_time;
    }

    public void setValidate_code_time(Date validate_code_time) {
        this.validate_code_time = validate_code_time;
    }

    public Date getSms_validate_code_time() {
        return sms_validate_code_time;
    }

    public void setSms_validate_code_time(Date sms_validate_code_time) {
        this.sms_validate_code_time = sms_validate_code_time;
    }
}
