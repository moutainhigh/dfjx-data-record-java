package com.datarecord.webapp.sys.origin.service;

import com.datarecord.webapp.sys.origin.entity.Origin;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface OriginService {

    List<Origin> listAllOrigin();
    PageResult listOrigin(int currPage, int pageSize);
    void createOrigin(Origin origin);
    Map<String,Object> getOriginById(Integer origin_id);

    void userOriginSave(Integer originId, Integer userId);

    Origin getOriginByUser(Integer userId);

    List<Origin> checkAllChildren(Integer originId);

    List<Origin> checkoutSons(Integer parentOriginId, List<Origin> originList);

    Map<String,Origin> getFist2Origin(Integer checkOrigin, List<Origin> allOrigins);

    Collection<Map<String, Object>> checkProvAndCity(List<Origin> allOrigins);

    List<Origin> getOriginByName(String searchOriginName);

    Origin getOriginTree(List<Integer> childrenOrigins, List<Origin> allOrigins);

    List<User> getUsersByOrigin(Integer originId);

    void updateOrigin(Origin origin);

    void removeUserOrigin(int userId);

    Origin getAllOriginTree();
}
