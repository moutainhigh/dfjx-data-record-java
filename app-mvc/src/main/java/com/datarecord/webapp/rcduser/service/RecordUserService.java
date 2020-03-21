package com.datarecord.webapp.rcduser.service;

import com.datarecord.webapp.rcduser.bean.Originss;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.rcduser.bean.RecordUser;
import com.datarecord.webapp.rcduser.bean.Useroriginassign;
import com.webapp.support.page.PageResult;

import java.util.List;

public interface RecordUserService {

 /*   String selectOrgId(int user_id);
*/
    List<Originss> listOrgData(String orgId);

    PageResult rcdpersonconfiglist(int currPage, int pageSize, String user_name);

    List<Object> useroriginassignlist(String origin_id);

    void insertrcdpersonconfig(String origin_id, String userid);

    List<Useroriginassign> selectrcdpersonconfig(String origin_id);


    void deletercdpersonconfigbyuserid(String user_id);

    List<Object> useroriginassignlistsysorigin(String origin_id);

   List<RecordUser> rcdpersonconfiglistwufenye();

    PageResult unCheckOriginUser(String currPage, String pageSize,String jobId, String originId);

    PageResult checkedOriginUser(String currPage, String pageSize, String jobId, String originId);

    void updaterRdpersonconfig(String origin_id, String userid);

    int countRcdPersonConfig(String userid);
}
