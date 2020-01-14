package submittedBy.service;

import com.webapp.support.page.PageResult;
import submittedBy.bean.Useroriginassign;
import utils.EntityTree;

import java.util.List;

public interface SubmittedByService {
    
    String selectOrgId(int user_id);

    List<EntityTree> listOrgData(String orgId);

    PageResult rcdpersonconfiglist(int currPage, int pageSize, String user_name);

    List<Useroriginassign> useroriginassignlist(String origin_id);

    void insertrcdpersonconfig(String origin_id, String[] userid);

    List<Useroriginassign> selectrcdpersonconfig(String origin_id);

    void deletercdpersonconfig(String origin_id);

    void deletercdpersonconfigbyuserid(String user_id);
}
