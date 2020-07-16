package com.datarecord.webapp.notice.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ReportSmsConfig {

    private Integer id;
    private String config_name;
    private String report_defined_id;
    private String report_defined_name;
    private String sms_template_id;
    private String sms_template_name;
    private Date send_date;
    private String send_date_str;
    private String send_time_str;
    private Date send_time;
    private String cross_holiday;

    private String distance_days;
    private String distance_type;
    private String config_status;

    /*
    0:待发送
    1:发送中
    2:已发送
    3:发送失败
     */
    private String send_status;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConfig_name() {
        return config_name;
    }

    public void setConfig_name(String config_name) {
        this.config_name = config_name;
    }

    public String getReport_defined_id() {
        return report_defined_id;
    }

    public void setReport_defined_id(String report_defined_id) {
        this.report_defined_id = report_defined_id;
    }

    public String getSms_template_id() {
        return sms_template_id;
    }

    public void setSms_template_id(String sms_template_id) {
        this.sms_template_id = sms_template_id;
    }

    public Date getSend_date() {
        return send_date;
    }

    public void setSend_date(Date send_date) {
        this.send_date = send_date;
        if(this.send_date!=null){
            this.send_date_str = new SimpleDateFormat("yyyy-MM-dd").format(this.send_date);
        }
    }

    public Date getSend_time() {
        return send_time;
    }

    public void setSend_time(Date send_time) {
        this.send_time = send_time;
        if(this.send_time!=null){
            this.send_time_str = new SimpleDateFormat("HH:mm").format(this.send_time);
        }
    }

    public String getCross_holiday() {
        return cross_holiday;
    }

    public void setCross_holiday(String cross_holiday) {
        this.cross_holiday = cross_holiday;
    }

    public String getDistance_days() {
        return distance_days;
    }

    public void setDistance_days(String distance_days) {
        this.distance_days = distance_days;
    }

    public String getSend_status() {
        return send_status;
    }

    public void setSend_status(String send_status) {
        this.send_status = send_status;
    }

    public String getSend_time_str() {
        return send_time_str;
    }

    public void setSend_time_str(String send_time_str) {
        this.send_time_str = send_time_str;
    }

    public String getSend_date_str() {
        return send_date_str;
    }

    public void setSend_date_str(String send_date_str) {
        this.send_date_str = send_date_str;
    }

    public String getReport_defined_name() {
        return report_defined_name;
    }

    public void setReport_defined_name(String report_defined_name) {
        this.report_defined_name = report_defined_name;
    }

    public String getDistance_type() {
        return distance_type;
    }

    public void setDistance_type(String distance_type) {
        this.distance_type = distance_type;
    }

    public String getSms_template_name() {
        return sms_template_name;
    }

    public void setSms_template_name(String sms_template_name) {
        this.sms_template_name = sms_template_name;
    }

    public String getConfig_status() {
        return config_status;
    }

    public void setConfig_status(String config_status) {
        this.config_status = config_status;
    }
}
