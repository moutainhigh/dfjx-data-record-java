package com.datarecord.webapp.rcduser.service.imp;

import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.rcduser.bean.Originss;
import com.datarecord.webapp.rcduser.bean.RecordUser;
import com.datarecord.webapp.rcduser.bean.Useroriginassign;
import com.datarecord.webapp.rcduser.dao.IRecordUserDao;
import com.datarecord.webapp.rcduser.service.RecordUserService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.utils.EntityTree;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("recordUserService")
public class RecordUserServiceImp implements RecordUserService {

    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private IRecordUserDao recordUserDao;

    @Autowired
    private OriginService originService;

   /* @Override
    public String selectOrgId(int user_id) {
        String orgId = recordUserDao.getOrgId(user_id);
        return orgId;
    }
*/
    @Override
    public List<Originss> listOrgData(String orgId) {
        List<Originss> entityTrees = recordUserDao.listOrgData(orgId);
        return  entityTrees;
    }

    @Override
    public PageResult rcdpersonconfiglist(int currPage, int pageSize, String user_name) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
      /*  List<Origin> childrenOrigins = originService.checkAllChildren(userOrigin.getOrigin_id());
        List<Integer> originIds  = new ArrayList<>();
        for (com.datarecord.webapp.sys.origin.entity.Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id()); }
        Page<RecordUser> contactPageDatas = null;
        if(originIds.size() != 0){
            contactPageDatas = recordUserDao.rcdpersonconfiglist(currPage, pageSize,user_name,originIds);
        }else {
            String originid  = userOrigin.getOrigin_id().toString();
            contactPageDatas = recordUserDao.rcdpersonconfiglistByid(currPage, pageSize,user_name);
        }*/
        Page<RecordUser> contactPageDatas  = recordUserDao.rcdpersonconfiglistByid(currPage, pageSize,user_name);

        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    public List<Object> useroriginassignlist(String origin_id) {
      List<Object> list = new ArrayList<>();
        List<Origin> childrenOrigins = originService.checkAllChildren(Integer.valueOf(origin_id));
        List<Integer> originIds  = new ArrayList<>();
        for (com.datarecord.webapp.sys.origin.entity.Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id()); }
        List<EntityTree> lists = null;
        if (originIds.size()!= 0){
           lists  =  recordUserDao.listOrgDatauser(originIds);
        }else{
            lists  =  recordUserDao.listOrgDatauserByid(origin_id);
        }
        for (EntityTree x : lists) {
                    Map<String,String> mapArr = new LinkedHashMap<>();
                    mapArr.put("user_id", x.getUser_id());
                    mapArr.put("user_name_cn", x.getUser_name_cn());
                    list.add(mapArr);
        }
        return list;
    }


    @Override
    public List<Object> useroriginassignlistsysorigin(String origin_id) {
        List<Object> list = new ArrayList<>();
        List<Origin> childrenOrigins = originService.checkAllChildren(Integer.valueOf(origin_id));
        List<Integer> originIds  = new ArrayList<>();
        for (com.datarecord.webapp.sys.origin.entity.Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id()); }
        List<EntityTree> lists =  recordUserDao.useroriginassignlistsysorigin(originIds);
        for (EntityTree x : lists) {
                    Map<String,String> mapArr = new LinkedHashMap<>();
                    mapArr.put("user_id", x.getUser_id());
                    mapArr.put("user_name_cn", x.getUser_name_cn());
                    list.add(mapArr);
        }
        return list;
    }

    @Override
    public List<RecordUser> rcdpersonconfiglistwufenye() {
       /* List<Origin> childrenOrigins = originService.checkAllChildren(userOrigin.getOrigin_id());
        List<Integer> originIds  = new ArrayList<>();
        for (com.datarecord.webapp.sys.origin.entity.Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id()); }
        if(originIds.size() != 0){
            return recordUserDao.rcdpersonconfiglistwufenye(originIds);
        }else {
            String originid  = userOrigin.getOrigin_id().toString();
            return recordUserDao.rcdpersonconfiglistwufeByid(originid);
        }*/
        return recordUserDao.rcdpersonconfiglistwufeByid();
    }

    @Override
    public PageResult unCheckOriginUser(String currPage, String pageSize,String jobId, String originId) {
        Page<User> recordUserPage = recordUserDao.getOriginRecordUser(currPage,pageSize,jobId,originId);
        PageResult pageResult = PageResult.pageHelperList2PageResult(recordUserPage);
        return pageResult;
    }

    @Override
    public PageResult checkedOriginUser(String currPage, String pageSize, String jobId, String originId) {
        Page<User> recordUserPage = recordUserDao.checkedOriginUser(currPage,pageSize,jobId,originId);
        PageResult pageResult = PageResult.pageHelperList2PageResult(recordUserPage);
        return pageResult;
    }




    @Override
    public void insertrcdpersonconfig(String origin_id, String userid) {
        String[] split = userid.split(",");
        for (String user_id : split){
            List<String> orgId = recordUserDao.selectuserid(user_id);
                for(String orgid : orgId){
                    recordUserDao.insertrcdpersonconfig(orgid,user_id);
                }
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updaterRdpersonconfig(String origin_id, String userid) {
        recordUserDao.deletercdpersonconfig(origin_id);
        String[] split = userid.split(",");
        for (String user_id : split){
            List<String> orgId = recordUserDao.selectuserid(user_id);
            for(String orgid : orgId){
                recordUserDao.insertrcdpersonconfig(orgid,user_id);
            }
        }
    }

    @Override
    public List<Useroriginassign> selectrcdpersonconfig(String origin_id) {
        return recordUserDao.selectrcdpersonconfig(origin_id);
    }


    @Override
    public void deletercdpersonconfigbyuserid(String user_id) {
        recordUserDao.deletercdpersonconfigbyuserid(user_id);
    }


}
