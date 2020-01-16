package com.datarecord.webapp.datadictionary.bean;

public class DataDictionary {

    private int dict_id; //数据字典编码
    private String dict_name;  //数据字典名称

    private int dict_content_id;   //数据字典内容编码
    private String dict_content_name;  //数据字典内容名称
   private String dict_content_value;   //数据字典内容值


    public int getDict_id() {
        return dict_id;
    }

    public void setDict_id(int dict_id) {
        this.dict_id = dict_id;
    }

    public String getDict_name() {
        return dict_name;
    }

    public void setDict_name(String dict_name) {
        this.dict_name = dict_name;
    }

    public int getDict_content_id() {
        return dict_content_id;
    }

    public void setDict_content_id(int dict_content_id) {
        this.dict_content_id = dict_content_id;
    }

    public String getDict_content_name() {
        return dict_content_name;
    }

    public void setDict_content_name(String dict_content_name) {
        this.dict_content_name = dict_content_name;
    }

    public String getDict_content_value() {
        return dict_content_value;
    }

    public void setDict_content_value(String dict_content_value) {
        this.dict_content_value = dict_content_value;
    }

    @Override
    public String toString() {
        return "DataDictionary{" +
                "dict_id=" + dict_id +
                ", dict_name='" + dict_name + '\'' +
                ", dict_content_id=" + dict_content_id +
                ", dict_content_name='" + dict_content_name + '\'' +
                ", dict_content_value='" + dict_content_value + '\'' +
                '}';
    }
}
