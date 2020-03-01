package com.datarecord.webapp.dataindex.bean;

public class RcdDt {

    private int  proj_id;      //类型编码   基本类别  人 地 物
    private String proj_name;  //类型名称

    private int  catg_id;     //类型编码
    private String catg_name;  //类型名称
    private String fld_status;

    private int  fld_id;     //指标id
    private String fld_name;  //指标名称
    private int  fld_type;   //指标类型 0:通用指标 1:突发指标
    private String fld_point;    //指标单位
    private String fld_data_type;  //0:字符串 1:数字 2:日期 3:
    private int  fld_is_null;

    private int fld_range;   //取值范围：0-所有、1-移动端、2-PC端
    private int fld_visible;  //可见范围：0-全部、1-移动端可见、2-PC端可见

    public String getFld_status() {
        return fld_status;
    }

    public void setFld_status(String fld_status) {
        this.fld_status = fld_status;
    }

    public int getFld_range() {
        return fld_range;
    }

    public void setFld_range(int fld_range) {
        this.fld_range = fld_range;
    }

    public int getFld_visible() {
        return fld_visible;
    }

    public void setFld_visible(int fld_visible) {
        this.fld_visible = fld_visible;
    }

    public int getFld_is_null() {
        return fld_is_null;
    }

    public void setFld_is_null(int fld_is_null) {
        this.fld_is_null = fld_is_null;
    }

    public int getProj_id() {
        return proj_id;
    }

    public void setProj_id(int proj_id) {
        this.proj_id = proj_id;
    }

    public String getProj_name() {
        return proj_name;
    }

    public void setProj_name(String proj_name) {
        this.proj_name = proj_name;
    }

    public int getCatg_id() {
        return catg_id;
    }

    public void setCatg_id(int catg_id) {
        this.catg_id = catg_id;
    }

    public String getCatg_name() {
        return catg_name;
    }

    public void setCatg_name(String catg_name) {
        this.catg_name = catg_name;
    }

    public int getFld_id() {
        return fld_id;
    }

    public void setFld_id(int fld_id) {
        this.fld_id = fld_id;
    }

    public String getFld_name() {
        return fld_name;
    }

    public void setFld_name(String fld_name) {
        this.fld_name = fld_name;
    }

    public int getFld_type() {
        return fld_type;
    }

    public void setFld_type(int fld_type) {
        this.fld_type = fld_type;
    }

    public String getFld_point() {
        return fld_point;
    }

    public void setFld_point(String fld_point) {
        this.fld_point = fld_point;
    }

    public String getFld_data_type() {
        return fld_data_type;
    }

    public void setFld_data_type(String fld_data_type) {
        this.fld_data_type = fld_data_type;
    }

    @Override
    public String toString() {
        return "RcdDt{" +
                "proj_id=" + proj_id +
                ", proj_name='" + proj_name + '\'' +
                ", catg_id=" + catg_id +
                ", catg_name='" + catg_name + '\'' +
                ", fld_status='" + fld_status + '\'' +
                ", fld_id=" + fld_id +
                ", fld_name='" + fld_name + '\'' +
                ", fld_type=" + fld_type +
                ", fld_point='" + fld_point + '\'' +
                ", fld_data_type='" + fld_data_type + '\'' +
                ", fld_is_null=" + fld_is_null +
                ", fld_range=" + fld_range +
                ", fld_visible=" + fld_visible +
                '}';
    }
}
