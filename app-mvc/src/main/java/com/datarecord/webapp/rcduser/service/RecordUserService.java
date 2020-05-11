package com.datarecord.webapp.rcduser.service;

import com.datarecord.webapp.rcduser.bean.RecordUserGroup;
import com.datarecord.webapp.rcduser.bean.RecordUser;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;

import java.util.List;

public interface RecordUserService {

 /*   String selectOrgId(int user_id);
*/
    PageResult pageRecordUserGroup(String currPage, String pageNum);

    void saveUserGroup(String groupName);

    void updateUserGroup(RecordUserGroup recordUserGroup);

    void delUserGroup(String groupId);

    void activeUserGroup(String groupId);

    List<User> groupUsers(String groupId);

    List<Origin> groupOrigins(String groupId);

    List<String> groupOriginIds(String groupId);

    List<RecordUser> groupOriginUsers(String groupId, List<String> originIds);

    void addUserToGroup(RecordUser recordUser);

    void delUserFromGroup(RecordUser recordUser);

    PageResult unCheckOriginUser(String currPage, String pageSize,String jobId, String originId);

    PageResult checkedOriginUser(String currPage, String pageSize, String jobId, String originId);

    RecordUserGroup getActiveUserGroup();

   List<Origin> getJobOriginHis(String jobId);

    List<User> getJobUserHis(String jobId);

    List<RecordUserGroup> jobUserGroupHis(String jobId);

    List<RecordUserGroup> getUserGroupList();
}
