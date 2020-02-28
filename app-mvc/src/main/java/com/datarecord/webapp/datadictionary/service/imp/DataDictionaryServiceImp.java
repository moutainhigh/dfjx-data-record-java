package com.datarecord.webapp.datadictionary.service.imp;

import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.datadictionary.dao.DataDictionaryDao;
import com.datarecord.webapp.datadictionary.service.DataDictionaryService;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("dataDictionaryService")
public class DataDictionaryServiceImp  implements DataDictionaryService {

    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private DataDictionaryDao dataDictionaryDao;

    @Override
    public void insertDataDictionary(String dict_name) {
        dataDictionaryDao.insertDataDictionary(dict_name);
    }

    @Override
    public PageResult selectDataDictionary(int currPage, int pageSize, String dict_id) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        Page<DataDictionary> contactPageDatas = dataDictionaryDao.selectDataDictionary(currPage, pageSize,dict_id);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    public void inserttypeDataDictionary(String dict_id, String dict_content_name, String dict_content_value) {
        dataDictionaryDao.inserttypeDataDictionary(dict_id,dict_content_name,dict_content_value);
    }

    @Override
    public void updateDataDictionary(String dict_content_id, String dict_id, String dict_content_name, String dict_content_value) {
        dataDictionaryDao.updateDataDictionary(dict_content_id,dict_id,dict_content_name,dict_content_value);
    }

    @Override
    public List<DataDictionary>  selectleftDataDictionary() {
        return dataDictionaryDao.selectleftDataDictionary();
    }

    @Override
    public void updateDataDictionarybydictid(String dict_id, String dict_name) {
         dataDictionaryDao.updateDataDictionarybydictid(dict_id,dict_name);
    }

    @Override
    public PageResult dataDictionarylist(int currPage, int pageSize) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        Page<DataDictionary> contactPageDatas = dataDictionaryDao.dataDictionarylist(currPage, pageSize);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    public void deleteDataDictionarybydictid(String dict_id) {
        dataDictionaryDao.deleteDataDictionarybydictid(dict_id);
    }

    @Override
    public void deleteDataDictionary(String dict_content_id) {
        dataDictionaryDao.deleteDataDictionary(dict_content_id);
    }

    @Override
    public int selectcountrcddtfldctassign(String dict_content_id) {
        return  dataDictionaryDao.selectcountrcddtfldctassign(dict_content_id);
    }

    @Override
    public int selectbyrcddtdictcontentdictid(String dict_id) {
        return  dataDictionaryDao.selectbyrcddtdictcontentdictid(dict_id);
    }
}
