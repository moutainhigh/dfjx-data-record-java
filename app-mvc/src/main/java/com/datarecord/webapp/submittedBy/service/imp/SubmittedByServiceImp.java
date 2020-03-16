package com.datarecord.webapp.submittedBy.service.imp;

import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.submittedBy.bean.Origin;
import com.datarecord.webapp.submittedBy.bean.SubmittedBy;
import com.datarecord.webapp.submittedBy.bean.Useroriginassign;
import com.datarecord.webapp.submittedBy.dao.SubmittedByDao;
import com.datarecord.webapp.submittedBy.service.SubmittedByService;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.utils.EntityTree;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("submittedByService")
public class SubmittedByServiceImp  implements SubmittedByService {

    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private SubmittedByDao submittedByDao;

    @Autowired
    private OriginService originService;

   /* @Override
    public String selectOrgId(int user_id) {
        String orgId = submittedByDao.getOrgId(user_id);
        return orgId;
    }
*/
    @Override
    public List<Origin> listOrgData(String orgId) {
        List<Origin> entityTrees = submittedByDao.listOrgData(orgId);
        return  entityTrees;
    }

    @Override
    public PageResult rcdpersonconfiglist(int currPage, int pageSize, String user_name) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        Page<SubmittedBy> contactPageDatas = submittedByDao.rcdpersonconfiglist(currPage, pageSize,user_name);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    public List<Object> useroriginassignlist(String origin_id) {
      List<EntityTree> menuCommon;
      Map<String,String> mapArray = new LinkedHashMap<String,String>();
      List<Object> list = new ArrayList<Object>();
        List<com.datarecord.webapp.sys.origin.entity.Origin> childrenOrigins = originService.checkAllChildren(Integer.valueOf(origin_id));
        List<String> originIds  = new ArrayList<>();
        for (com.datarecord.webapp.sys.origin.entity.Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id().toString()); }
        List<EntityTree> lists  =  submittedByDao.listOrgDatauser();
       menuCommon  = lists;
        for (EntityTree x : menuCommon) {
            for (String id : originIds){
                if (x.getId().equals(id)){
                    Map<String,String> mapArr = new LinkedHashMap<>();
                    mapArr.put("user_id", x.getUser_id());
                    mapArr.put("user_name_cn", x.getUser_name_cn());
                    // this.menuChild(x.getId());
                    list.add(mapArr);
                }
            }
        }
        return list;
    }


    @Override
    public List<Object> useroriginassignlistsysorigin(String origin_id) {
        List<EntityTree> menuCommon;
        List<Object> list = new ArrayList<Object>();
        List<com.datarecord.webapp.sys.origin.entity.Origin> childrenOrigins = originService.checkAllChildren(Integer.valueOf(origin_id));
        List<String> originIds  = new ArrayList<>();
        for (com.datarecord.webapp.sys.origin.entity.Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id().toString()); }
        List<EntityTree> lists =  submittedByDao.useroriginassignlistsysorigin();
        menuCommon  = lists;
        for (EntityTree x : menuCommon) {
            for (String id : originIds){
                if (x.getId().equals(id)){
                    Map<String,String> mapArr = new LinkedHashMap<>();
                    mapArr.put("user_id", x.getUser_id());
                    mapArr.put("user_name_cn", x.getUser_name_cn());
                    list.add(mapArr);
                }
            }
        }
        return list;
    }

    @Override
    public List<SubmittedBy> rcdpersonconfiglistwufenye() {
        return submittedByDao.rcdpersonconfiglistwufenye();
    }


    /*@Override
    public void insertrcdpersonconfig(String origin_id, String userid) {
      *//*  userid.substring(1);
        userid.substring(0,userid.length()-1);*//*
        String[] split = userid.split(",");
        for (String user_id : split){
            submittedByDao.insertrcdpersonconfig(origin_id,user_id);
        }
    }*/
    @Override
    public void insertrcdpersonconfig(String origin_id, String userid) {
        String[] split = userid.split(",");
        for (String user_id : split){
            List<String> orgId = submittedByDao.selectuserid(user_id);
            //for (int i=0; i<orgId.size(); i++){
                for(String orgid : orgId){
                    submittedByDao.insertrcdpersonconfig(orgid,user_id);
                }

           // }
        }
    }

    @Override
    public List<Useroriginassign> selectrcdpersonconfig(String origin_id) {
        return submittedByDao.selectrcdpersonconfig(origin_id);
    }

    @Override
    public void deletercdpersonconfig(String origin_id) {
        submittedByDao.deletercdpersonconfig(origin_id);
    }

    @Override
    public void deletercdpersonconfigbyuserid(String user_id) {
        submittedByDao.deletercdpersonconfigbyuserid(user_id);
    }


}
