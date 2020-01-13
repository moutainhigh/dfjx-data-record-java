package datadictionary.service;

import com.webapp.support.page.PageResult;

public interface DataDictionaryService {

    void insertDataDictionary(String dict_name);

    PageResult selectDataDictionary(int currPage, int pageSize, String dict_id);

    void inserttypeDataDictionary(String dict_id, String dict_content_name, String dict_content_value);

    void updateDataDictionary(String dict_content_id, String dict_id, String dict_content_name, String dict_content_value);
}
