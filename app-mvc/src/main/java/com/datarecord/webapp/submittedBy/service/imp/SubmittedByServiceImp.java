package com.datarecord.webapp.submittedBy.service.imp;

import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.submittedBy.bean.SubmittedBy;
import com.datarecord.webapp.submittedBy.bean.Useroriginassign;
import com.datarecord.webapp.submittedBy.dao.SubmittedByDao;
import com.datarecord.webapp.submittedBy.service.SubmittedByService;
import com.datarecord.webapp.utils.EntityTree;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("submittedByService")
public class SubmittedByServiceImp  implements SubmittedByService {

    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private SubmittedByDao submittedByDao;

    @Override
    public String selectOrgId(int user_id) {
        String orgId = submittedByDao.getOrgId(user_id);
        return orgId;
    }

    @Override
    public List<EntityTree> listOrgData() {
        List<EntityTree> entityTrees = submittedByDao.listOrgData();
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
    public List<Useroriginassign> useroriginassignlist(String origin_id) {
        return submittedByDao.useroriginassignlist(origin_id);
    }

    @Override
    public void insertrcdpersonconfig(String origin_id, String userid) {
      /*  userid.substring(1);
        userid.substring(0,userid.length()-1);*/
        String[] split = userid.split(",");
        for (String user_id : split){
            submittedByDao.insertrcdpersonconfig(origin_id,user_id);
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
