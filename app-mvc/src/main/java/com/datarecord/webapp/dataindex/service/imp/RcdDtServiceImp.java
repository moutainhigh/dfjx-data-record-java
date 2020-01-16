package com.datarecord.webapp.dataindex.service.imp;

import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.dataindex.bean.RcdDtFld;
import com.datarecord.webapp.dataindex.bean.RcddtCatg;
import com.datarecord.webapp.dataindex.bean.Rcddtproj;
import com.datarecord.webapp.dataindex.dao.RcdDtDao;
import com.datarecord.webapp.dataindex.service.RcdDtService;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("rcdDtService")
public class RcdDtServiceImp  implements RcdDtService {


    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private RcdDtDao rcdDtDao;


    @Override
    public void insertrcddtproj(String proj_name, String is_actived) {
        rcdDtDao.insertrcddtproj(proj_name,is_actived);
    }

    @Override
    public PageResult selectrcddtproj(int currPage, int pageSize) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        Page<DataDictionary> contactPageDatas = rcdDtDao.selectrcddtproj(currPage, pageSize);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    public void updatercddtproj(String proj_name, String is_actived,String proj_id) {
        rcdDtDao.updatercddtproj(proj_name,is_actived,proj_id);
    }

    @Override
    public PageResult selecttixircddtproj(int currPage, int pageSize, String catg_id) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        Page<DataDictionary> contactPageDatas = rcdDtDao.selecttixircddtproj(currPage, pageSize,catg_id);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    public void insertrcddtfld(String catg_id, String fld_name, String fld_point, String fld_type, String fld_data_type, String fld_is_null) {
        rcdDtDao.insertrcddtfld(catg_id,fld_name,fld_point,fld_type,fld_data_type,fld_is_null);
    }

    @Override
    public void insertrcddtfldctassign(String fld_id, String dict_content_id) {
        rcdDtDao.insertrcddtfldctassign(fld_id,dict_content_id);
    }

    @Override
    public void updatercddtdict(String dict_content_id) {
        rcdDtDao.updatercddtdict(dict_content_id);
    }

    @Override
    public void deletercddtfldctassign(String fld_id) {
        rcdDtDao.deletercddtfldctassign(fld_id);
    }

    @Override
    public void updatercddtfld(String fld_id, String catg_id, String fld_name, String fld_point, String fld_type, String fld_data_type, String fld_is_null) {
        rcdDtDao.updatercddtfld(fld_id,catg_id,fld_name,fld_point,fld_type,fld_data_type,fld_is_null);
    }


    @Override
    public List<Rcddtproj> leftrcddtprojjblx() {
        return rcdDtDao.leftrcddtprojjblx();
    }

    @Override
    public List<RcddtCatg> leftrcddtcatglx(String proj_id) {
        return rcdDtDao.leftrcddtcatglx(proj_id);
    }

    @Override
    public List<RcdDtFld> leftrcddtfld(String catg_id) {
        return rcdDtDao.leftrcddtfld(catg_id);
    }

    @Override
    public PageResult selecttixircddtprojer(int currPage, int pageSize, String proj_id) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        Page<DataDictionary> contactPageDatas = rcdDtDao.selecttixircddtprojer(currPage, pageSize,proj_id);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }


}
