package com.datarecord.webapp.sys.user.entity;

import com.webapp.support.json.JsonSupport;

import java.util.Date;

public class CustomerUser {
    private String origin_city;
    private String origin_province;

    private int user_id;
    private String user_pwd;
    private String old_user_pwd;
    private String user_name;
    private String user_name_cn;
    private String user_type;
    private Date reg_date;
    private String user_status;
    private Date last_login_time;
    private Integer  origin_id;
    private String  origin_name;
    private String office_phone;
    private String mobile_phone;
    private String email;
    private String social_code;

    public int getUser_id()
    {
        return this.user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        if (this.user_name == null) {
            this.user_name = "";
        }
        return this.user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }



    public String toString()
    {

        return JsonSupport.objectToJson(this);

//        return "User [user_id=" + this.user_id + ", user_nm=" + this.user_nm + ", user_real_nm=" + this.user_real_nm + ", user_pwd=" + this.user_pwd + ", active_ind=" + this.active_ind + ", superior_id=" + this.superior_id + ", sign_in_ip=" + this.sign_in_ip + ", user_group_id=" + this.user_group_id + ", group_name=" + this.group_name + ", role_id=" + this.role_id + ", create_id=" + this.create_id + ", create_name=" + this.create_name + ", create_ts=" + this.create_ts + ", str_create_time=" + this.str_create_time + ", update_id=" + this.update_id + ", update_name=" + this.update_name + ", update_ts=" + this.update_ts + ", str_update_time=" + this.str_update_time + ", menuList=" + this.menuList + ", roleList=" + this.roleList + "]";
    }

    public String getUser_pwd() {
        return user_pwd;
    }

    public void setUser_pwd(String user_pwd) {
        this.user_pwd = user_pwd;
    }

    public String getUser_name_cn() {
        return user_name_cn;
    }

    public void setUser_name_cn(String user_name_cn) {
        this.user_name_cn = user_name_cn;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }
    public Integer getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(Integer origin_id) {
        this.origin_id = origin_id;
    }

    public String getOrigin_city() {
        return origin_city;
    }

    public void setOrigin_city(String origin_city) {
        this.origin_city = origin_city;
    }

    public String getOrigin_province() {
        return origin_province;
    }

    public void setOrigin_province(String origin_province) {
        this.origin_province = origin_province;
    }

    public Date getReg_date() {
        return reg_date;
    }

    public void setReg_date(Date reg_date) {
        this.reg_date = reg_date;
    }

    public Date getLast_login_time() {
        return last_login_time;
    }

    public void setLast_login_time(Date last_login_time) {
        this.last_login_time = last_login_time;
    }

    public String getOld_user_pwd() {
        return old_user_pwd;
    }

    public void setOld_user_pwd(String old_user_pwd) {
        this.old_user_pwd = old_user_pwd;
    }

    public String getOffice_phone() {
        return office_phone;
    }

    public void setOffice_phone(String office_phone) {
        this.office_phone = office_phone;
    }

    public String getMobile_phone() {
        return mobile_phone;
    }

    public void setMobile_phone(String mobile_phone) {
        this.mobile_phone = mobile_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSocial_code() {
        return social_code;
    }

    public void setSocial_code(String social_code) {
        this.social_code = social_code;
    }
}
