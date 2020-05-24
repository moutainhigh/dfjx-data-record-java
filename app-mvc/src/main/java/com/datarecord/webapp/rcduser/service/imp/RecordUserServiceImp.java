package com.datarecord.webapp.rcduser.service.imp;

import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.rcduser.bean.RecordUser;
import com.datarecord.webapp.rcduser.bean.RecordUserGroup;
import com.datarecord.webapp.rcduser.dao.IRecordUserDao;
import com.datarecord.webapp.rcduser.service.RecordUserService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.utils.DataRecordUtil;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Service("recordUserService")
public class RecordUserServiceImp implements RecordUserService {

    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private IRecordUserDao recordUserDao;

    @Autowired
    private OriginService originService;

    @Override
    public PageResult pageRecordUserGroup(String currPage, String pageNum) {
        String userId = null;
        if(!DataRecordUtil.isSuperUser()){
            userId = WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id().toString();
        }

        Page<RecordUserGroup> pageData = recordUserDao.pageRecordUserGroup(currPage,pageNum,userId);
        PageResult pageResult = PageResult.pageHelperList2PageResult(pageData);
        return pageResult;
    }

    @Override
    public void saveUserGroup(String groupName) {
//        Boolean active = false;
//        Page<RecordUserGroup> pageResult = recordUserDao.pageRecordUserGroup(null, null);
//        int total = pageResult.size();
//        if(total==0)
//            active = true;
//        recordUserDao.saveUserGroup(groupName,active?"1":"0");
        String ACTIVE = "1";
        recordUserDao.saveUserGroup(groupName,ACTIVE,WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id_str());
    }

    @Override
    public void updateUserGroup(RecordUserGroup recordUserGroup) {
        recordUserDao.updateUserGroup(recordUserGroup);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delUserGroup(String groupId) {
        recordUserDao.delGroupPerson(groupId);
        recordUserDao.delUserGroup(groupId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void activeUserGroup(String groupId) {
        recordUserDao.disableGroups( groupId);
        recordUserDao.enableGroups( groupId);
    }

    @Override
    public List<User> groupUsers(String groupId) {
        List<User> groupUsers = recordUserDao.groupUsers(groupId);
       return groupUsers;
    }

    @Override
    public List<Origin> groupOrigins(String groupId) {
        List<Origin> groupOrigins = recordUserDao.groupOrigins(groupId);
        return groupOrigins;
    }

    @Override
    public List<String> groupOriginIds(String groupId) {
        List<Origin> groupOrigins = this.groupOrigins(groupId);
        List<String> originIds = new ArrayList<>();

        if(groupOrigins!=null&&groupOrigins.size()>0){
            for (Origin groupOrigin : groupOrigins) {
                BigInteger originId = groupOrigin.getOrigin_id();
                originIds.add(originId.toString());
            }
        }
        return originIds;
    }

    @Override
    public List<RecordUser> groupOriginUsers(String groupId, List<String> originIds) {
        List<RecordUser> recordUsers = recordUserDao.groupOriginUsers(groupId,originIds);
        return recordUsers;
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
    public RecordUserGroup getActiveUserGroup() {
        return recordUserDao.getActiveUserGroup();
    }

    @Override
    public List<Origin> getJobOriginHis(String jobId) {
        List<Origin> resultOrigins = recordUserDao.getJobOriginHis(jobId);
        return resultOrigins;
    }

    @Override
    public List<User> getJobUserHis(String jobId) {
        List<User> resultUsers = recordUserDao.getJobUserHis(jobId);
        return resultUsers;
    }

    @Override
    public List<RecordUserGroup> jobUserGroupHis(String jobId) {
        List<RecordUserGroup> jobUserGroup = recordUserDao.jobUserGroupHis(jobId);
        return jobUserGroup;
    }

    @Override
    public List<RecordUserGroup> getUserGroupList() {
        String userId = null;
        if(!DataRecordUtil.isSuperUser()){
            userId = WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id().toString();
        }
        List<RecordUserGroup> userGroupList = recordUserDao.getUserGroup(userId);
        return userGroupList;
    }


    @Override
    public void addUserToGroup(RecordUser recordUser) {
        recordUserDao.addRecordUser(recordUser);
    }

    @Override
    public void delUserFromGroup(RecordUser recordUser) {
        recordUserDao.delRecordUser(recordUser);
    }

}
