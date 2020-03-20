package com.datarecord.webapp.process.entity;

import com.datarecord.enums.FldDataTypes;
import com.google.common.base.Strings;

public class ReportFldConfig {

    private Integer job_unit_id;
    private String job_unit_name;
    private Integer catg_id;
    private String catg_name;
    private Integer fld_id;   //指标id
    private String fld_name; // 名称
    private String fld_point;
    private String fld_data_type;
    private String fld_data_type_str;
    private Integer fld_type; // 类型0:通用指标 1 突发指标
    private Integer fld_is_null;
    private String fld_is_null_str;
    private Integer is_actived;
    private Integer fld_range;    //取值范围：0-所有、1-移动端、2-PC端
    private Integer fld_visible; //可见范围：0-全部、1-移动端可见、2-PC端可见
    private Integer fld_status = 0;   //指标状态：  0：待审核  1：审核通过  2：审核驳回  3：作废'
    private Integer fld_creater;
    private String fld_creater_name;
    private Integer fld_creater_origin;
    private String fld_creater_origin_name;
    private String dict_content_id;

    public String getDict_content_id() {
        return dict_content_id;
    }

    public void setDict_content_id(String dict_content_id) {
        this.dict_content_id = dict_content_id;
    }

    public Integer getJob_unit_id() {
        return job_unit_id;
    }

    public void setJob_unit_id(Integer job_unit_id) {
        this.job_unit_id = job_unit_id;
    }

    public String getJob_unit_name() {
        return job_unit_name;
    }

    public void setJob_unit_name(String job_unit_name) {
        this.job_unit_name = job_unit_name;
    }

    public Integer getFld_id() {
        return fld_id;
    }

    public void setFld_id(Integer fld_id) {
        this.fld_id = fld_id;
    }

    public String getFld_name() {
        return fld_name;
    }

    public void setFld_name(String fld_name) {
        this.fld_name = fld_name;
    }


    public String toString(){
        return new StringBuilder().
                append("{fld_id:").append(fld_id).append(",").
                append("fld_name:").append(fld_name).append("}").
                append("fld_point:").append(fld_point).append("}").
                append("fld_data_type:").append(fld_data_type).append("}").
                append("fld_type:").append(fld_type).append("}").
                append("fld_is_null:").append(fld_is_null).append("}").
                append("is_actived:").append(is_actived).append("}").
                toString();
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
        if(!Strings.isNullOrEmpty(fld_data_type)){
            this.fld_data_type_str = FldDataTypes.getFldDataType(fld_data_type).getComment();
        }
    }

    public Integer getFld_type() {
        return fld_type;
    }

    public void setFld_type(Integer fld_type) {
        this.fld_type = fld_type;
    }

    public Integer getFld_is_null() {
        return fld_is_null;
    }

    public void setFld_is_null(Integer fld_is_null) {
        this.fld_is_null = fld_is_null;
        if(fld_is_null!=null){
            this.fld_is_null_str = fld_is_null==0?"可为空":"不可为空";
        }
    }

    public Integer getIs_actived() {
        return is_actived;
    }

    public void setIs_actived(Integer is_actived) {
        this.is_actived = is_actived;
    }

    public Integer getCatg_id() {
        return catg_id;
    }

    public void setCatg_id(Integer catg_id) {
        this.catg_id = catg_id;
    }

    public String getCatg_name() {
        return catg_name;
    }

    public void setCatg_name(String catg_name) {
        this.catg_name = catg_name;
    }

    public Integer getFld_range() {
        return fld_range;
    }

    public void setFld_range(Integer fld_range) {
        this.fld_range = fld_range;
    }

    public Integer getFld_visible() {
        return fld_visible;
    }

    public void setFld_visible(Integer fld_visible) {
        this.fld_visible = fld_visible;
    }

    public Integer getFld_status() {
        return fld_status;
    }

    public void setFld_status(Integer fld_status) {
        this.fld_status = fld_status;
    }

    public Integer getFld_creater() {
        return fld_creater;
    }

    public void setFld_creater(Integer fld_creater) {
        this.fld_creater = fld_creater;
    }

    public String getFld_creater_name() {
        return fld_creater_name;
    }

    public void setFld_creater_name(String fld_creater_name) {
        this.fld_creater_name = fld_creater_name;
    }

    public Integer getFld_creater_origin() {
        return fld_creater_origin;
    }

    public void setFld_creater_origin(Integer fld_creater_origin) {
        this.fld_creater_origin = fld_creater_origin;
    }

    public String getFld_creater_origin_name() {
        return fld_creater_origin_name;
    }

    public void setFld_creater_origin_name(String fld_creater_origin_name) {
        this.fld_creater_origin_name = fld_creater_origin_name;
    }

    public String getFld_data_type_str() {
        return fld_data_type_str;
    }

    public void setFld_data_type_str(String fld_data_type_str) {
        this.fld_data_type_str = fld_data_type_str;
    }

    public String getFld_is_null_str() {
        return fld_is_null_str;
    }

    public void setFld_is_null_str(String fld_is_null_str) {
        this.fld_is_null_str = fld_is_null_str;
    }
}
