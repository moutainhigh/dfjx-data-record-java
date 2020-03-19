package com.datarecord.webapp.rcduser.bean;


public class RecordUser {

       private int     user_id;
       private int       origin_id;
       private String        user_name;
       private String user_name_cn;
       private String   origin_name;

    public String getUser_name_cn() {
        return user_name_cn;
    }

    public void setUser_name_cn(String user_name_cn) {
        this.user_name_cn = user_name_cn;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(int origin_id) {
        this.origin_id = origin_id;
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
}
