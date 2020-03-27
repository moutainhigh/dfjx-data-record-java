package com.datarecord.webapp.sys.origin.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

public class Origin {

    private BigInteger origin_id;
    private String origin_id_str;
    private String origin_name;
    private BigInteger parent_origin_id;
    private String origin_status;
    private Date create_date;
    private String create_user;
    private String origin_type;
    private Integer origin_level;

    private String origin_address_province;
    private String origin_address_city;
    private String origin_address_area;
    private String origin_address_street;
    private String origin_address;
    private String origin_address_detail;
    private String origin_nature;

    private List<Origin> children;


    public BigInteger getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(BigInteger origin_id) {
        this.origin_id = origin_id;
        this.origin_id_str = origin_id.toString();
    }

    public String getOrigin_name() {
        return origin_name;
    }

    public void setOrigin_name(String origin_name) {
        this.origin_name = origin_name;
    }

    public BigInteger getParent_origin_id() {
        return parent_origin_id;
    }

    public void setParent_origin_id(BigInteger parent_origin_id) {
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

    public List<Origin> getChildren() {
        return children;
    }

    public void setChildren(List<Origin> children) {
        this.children = children;
    }

    public String getOrigin_type() {
        return origin_type;
    }

    public void setOrigin_type(String origin_type) {
        this.origin_type = origin_type;
    }

    public String getOrigin_address_province() {
        return origin_address_province;
    }

    public void setOrigin_address_province(String origin_address_province) {
        this.origin_address_province = origin_address_province;
    }

    public String getOrigin_address_city() {
        return origin_address_city;
    }

    public void setOrigin_address_city(String origin_address_city) {
        this.origin_address_city = origin_address_city;
    }

    public String getOrigin_address_area() {
        return origin_address_area;
    }

    public void setOrigin_address_area(String origin_address_area) {
        this.origin_address_area = origin_address_area;
    }

    public String getOrigin_address_street() {
        return origin_address_street;
    }

    public void setOrigin_address_street(String origin_address_street) {
        this.origin_address_street = origin_address_street;
    }

    public String getOrigin_address() {
        return origin_address;
    }

    public void setOrigin_address(String origin_address) {
        this.origin_address = origin_address;
    }

    public String getOrigin_address_detail() {
        return origin_address_detail;
    }

    public void setOrigin_address_detail(String origin_address_detail) {
        this.origin_address_detail = origin_address_detail;
    }

    public String getOrigin_nature() {
        return origin_nature;
    }

    public void setOrigin_nature(String origin_nature) {
        this.origin_nature = origin_nature;
    }

    public Integer getOrigin_level() {
        return origin_level;
    }

    public void setOrigin_level(Integer origin_level) {
        this.origin_level = origin_level;
    }

    public String getOrigin_id_str() {
        return origin_id_str;
    }

    public void setOrigin_id_str(String origin_id_str) {
        this.origin_id_str = origin_id_str;
    }
}
