package com.datarecord.webapp.submittedBy.service;

import com.datarecord.webapp.submittedBy.bean.Origin;
import com.datarecord.webapp.submittedBy.bean.Useroriginassign;
import com.datarecord.webapp.utils.EntityTree;
import com.webapp.support.page.PageResult;

import java.util.List;

public interface SubmittedByService {

 /*   String selectOrgId(int user_id);
*/
    List<Origin> listOrgData(String orgId);

    PageResult rcdpersonconfiglist(int currPage, int pageSize, String user_name);

    List<Object> useroriginassignlist(String origin_id);

    void insertrcdpersonconfig(String origin_id, String userid);

    List<Useroriginassign> selectrcdpersonconfig(String origin_id);

    void deletercdpersonconfig(String origin_id);

    void deletercdpersonconfigbyuserid(String user_id);

    List<Object> useroriginassignlistsysorigin(String origin_id);
}
