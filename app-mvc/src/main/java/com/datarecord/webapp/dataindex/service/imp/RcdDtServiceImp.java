package com.datarecord.webapp.dataindex.service.imp;

import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.dataindex.bean.*;
import com.datarecord.webapp.dataindex.dao.RcdDtDao;
import com.datarecord.webapp.dataindex.service.RcdDtService;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.datarecord.webapp.reportinggroup.dao.ReportingGroupDao;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.utils.EntityTree;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service("rcdDtService")
public class RcdDtServiceImp  implements RcdDtService {


    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private RcdDtDao rcdDtDao;

    @Autowired
    private ReportingGroupDao reportingGroupDao;


    @Autowired
    private OriginService originService;

    @Override
    public void insertrcddtproj(String proj_name, String is_actived) {
        rcdDtDao.insertrcddtproj(proj_name,is_actived);
    }

    @Override
    public PageResult selectrcddtproj(int currPage, int pageSize,Origin userOrigin) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        List<Origin> childrenOrigins = originService.checkAllChildren(userOrigin.getOrigin_id());
        childrenOrigins.add(0,userOrigin);
        List<Integer> originIds  = new ArrayList<>();
        for (Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id());
        }
        Page<DataDictionary> contactPageDatas = rcdDtDao.selectrcddtproj(currPage, pageSize,originIds);
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
    public void insertrcddtfld(ReportFldConfig reportFldConfig) {
        rcdDtDao.insertrcddtfld(reportFldConfig);
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
    public void updatercddtfld(ReportFldConfig reportFldConfig) {
        rcdDtDao.updatercddtfld(reportFldConfig);
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
    public PageResult selecttixircddtprojer(int currPage, int pageSize, String proj_id,Origin userOrigin) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        List<Origin> childrenOrigins = originService.checkAllChildren(userOrigin.getOrigin_id());
        childrenOrigins.add(0,userOrigin);
        List<Integer> originIds  = new ArrayList<>();
        for (Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id());
        }
        Page<DataDictionary> contactPageDatas = null;
        if(originIds.size()!=0){
          contactPageDatas = rcdDtDao.selecttixircddtprojer(currPage, pageSize,proj_id,originIds);
        }else{
            String originid = userOrigin.getOrigin_id().toString();
            contactPageDatas = rcdDtDao.selecttixircddtprojerByid(currPage, pageSize,proj_id,originid);
        }
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
    @Transactional(rollbackFor = Exception.class)
    public void deletercddtproj(String proj_id) {
        rcdDtDao.deletercddtproj(proj_id);  // rcd_dt_proj  一级  proj_id
        rcdDtDao.deletercddtcatg(proj_id); //rcd_dt_catg   二级   proj_id
    }

    //删除指标类别 二级
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletrcddtcatg(String catg_id) {
            rcdDtDao.deletercddtcatgi(catg_id); //rcd_dt_catg   二级   catg_id
            rcdDtDao.deletercddtfld(catg_id);  // rcd_dt_fld   三级  catg_id
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
    public List<String> selectrcddtcatg(String catg_id) {
        return rcdDtDao.selectrcddtcatgcatgid(catg_id);
    }

    @Override
    public List<String> selectrcddtcatgproj(String proj_id) {
        return rcdDtDao.selectrcddtcatg(proj_id);
    }


    @Override
    public void deleteererrcddtfldI(String catg_id) {
        rcdDtDao.deletercddtfldI(catg_id);
    }


}
