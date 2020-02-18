package com.datarecord.webapp.dataindex.service.imp;

import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.dataindex.bean.*;
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
        Page<RcdDt> contactPageDatas = rcdDtDao.selecttixircddtproj(currPage, pageSize,catg_id);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    public void insertrcddtfld(String catg_id, String fld_name, String fld_data_type, String fld_is_null,String fld_type,String fld_range,String fld_visible) {
        rcdDtDao.insertrcddtfld(catg_id,fld_name,fld_data_type,fld_is_null,fld_type,fld_range,fld_visible);
    }

    @Override
    public void insertrcddtfldctassign(String fld_id, String dict_contentid) {
        rcdDtDao.insertrcddtfldctassign(fld_id,dict_contentid);
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
    public void updatercddtfld(String fld_id, String catg_id, String fld_name, String fld_data_type, String fld_is_null,String fld_type,String fld_range,String fld_visible) {
        rcdDtDao.updatercddtfld(fld_id,catg_id,fld_name,fld_data_type,fld_is_null,fld_type,fld_range,fld_visible);
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

    @Override
    public int selectmax() {
      return  rcdDtDao.selectmax();
    }

    @Override
    public void inserttixircddtprojer(String catg_name, String proj_id) {
         rcdDtDao.inserttixircddtprojer(catg_name,proj_id);
    }

    @Override
    public void updatetixircddtprojer(String catg_id, String catg_name, String proj_id) {
         rcdDtDao.updatetixircddtprojer(catg_id,catg_name,proj_id);
    }

    @Override
    public List<RcdDtFldCtAssign> updatehuixianrcddtfldctassign(String fld_id) {
        return  rcdDtDao.updatehuixianrcddtfldctassign(fld_id);
    }

    //删除基本类型 一级以及关连二级三级
    @Override
    public void deletercddtproj(String proj_id) {
        rcdDtDao.deletercddtproj(proj_id);  // rcd_dt_proj  一级  proj_id
        rcdDtDao.deletercddtcatg(proj_id); //rcd_dt_catg   二级   proj_id
    }

    //删除指标类别 二级
    @Override
    public void deletrcddtcatg(String catg_id) {
            rcdDtDao.deletercddtcatgi(catg_id); //rcd_dt_catg   二级   catg_id

    }

    //三级
    @Override
    public void deletercddtfld(String fld_id) {
            rcdDtDao.deletercddtflds(fld_id);  //三级
    }

    @Override
    public int selectcount(String fld_id) {
        return rcdDtDao.selectcount(fld_id);
    }

    @Override
    public List<Object> selectrcddtcatg(String catg_id) {
        return rcdDtDao.selectrcddtcatgcatgid(catg_id);
    }

    @Override
    public List<Object> selectrcddtcatgproj(String proj_id) {
        return rcdDtDao.selectrcddtcatg(proj_id);
    }

    @Override
    public void deleteererrcddtfld(String catg_id) {
        rcdDtDao.deletercddtfld(catg_id);
    }

    @Override
    public void deleteererrcddtfldI(int i) {
        rcdDtDao.deletercddtfldI(i);
    }


}
