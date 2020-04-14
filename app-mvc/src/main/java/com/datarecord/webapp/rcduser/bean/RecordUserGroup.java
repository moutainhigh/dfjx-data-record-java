package com.datarecord.webapp.rcduser.bean;

import com.workbench.auth.user.entity.User;

import java.util.List;

public class RecordUserGroup {

    private Integer group_id;
    private String group_name;
    private Integer group_active;
    private List<User> record_users;

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public Integer getGroup_active() {
        return group_active;
    }

    public void setGroup_active(Integer group_active) {
        this.group_active = group_active;
    }

    public List<User> getRecord_users() {
        return record_users;
    }

    public void setRecord_users(List<User> record_users) {
        this.record_users = record_users;
    }
}
