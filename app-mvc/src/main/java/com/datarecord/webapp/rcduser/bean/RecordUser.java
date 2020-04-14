package com.datarecord.webapp.rcduser.bean;


import java.math.BigInteger;

public class RecordUser {

   private BigInteger user_id;
   private String user_id_str;
   private BigInteger origin_id;
   private String origin_id_str;
   private String user_name;
   private String user_name_cn;
   private String origin_name;
   private String group_id;
   private String group_name;

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
        if(user_id!=null){
            this.user_id_str = user_id.toString();
        }
    }

    public BigInteger getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(BigInteger origin_id) {
        this.origin_id = origin_id;
        if(origin_id!=null){
            this.origin_id_str = origin_id.toString();
        }
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    @Override
    public String toString() {
        return "RecordUser{" +
                "user_id=" + user_id +
                ", origin_id=" + origin_id +
                ", user_name='" + user_name + '\'' +
                ", user_name_cn='" + user_name_cn + '\'' +
                ", origin_name='" + origin_name + '\'' +
                '}';
    }

    public String getUser_id_str() {
        return user_id_str;
    }

    public void setUser_id_str(String user_id_str) {
        this.user_id_str = user_id_str;
    }

    public String getOrigin_id_str() {
        return origin_id_str;
    }

    public void setOrigin_id_str(String origin_id_str) {
        this.origin_id_str = origin_id_str;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
