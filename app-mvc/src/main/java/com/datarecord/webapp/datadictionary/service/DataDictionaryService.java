package com.datarecord.webapp.datadictionary.service;

import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.webapp.support.page.PageResult;

import java.util.List;

public interface DataDictionaryService {

    void insertDataDictionary(String dict_name);

    PageResult selectDataDictionary(int currPage, int pageSize, String dict_id);

    void inserttypeDataDictionary(String dict_id, String dict_content_name, String dict_content_value);

    void updateDataDictionary(String dict_content_id, String dict_id, String dict_content_name, String dict_content_value);

    List<DataDictionary> selectleftDataDictionary();

    void updateDataDictionarybydictid(String dict_id, String dict_name);

    PageResult dataDictionarylist(int currPage, int pageSize);

    void deleteDataDictionarybydictid(String dict_id);

    void deleteDataDictionary(String dict_content_id);

    int selectcountrcddtfldctassign(String dict_content_id);
}
