package com.datarecord.webapp.sys.origin.service;

import com.datarecord.webapp.sys.origin.entity.Origin;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface OriginService {

    List<Origin> listAllOrigin();
    PageResult listOrigin(int currPage, int pageSize);
    void createOrigin(Origin origin);
    Map<String,Object> getOriginById(BigInteger origin_id);

    void userOriginSave(BigInteger originId, BigInteger userId);

    Origin getOriginByUser(BigInteger userId);

    List<Origin> checkAllChildren(BigInteger originId);

    List<Origin> checkoutSons(BigInteger parentOriginId, List<Origin> originList);

    Map<String,Origin> getFist2Origin(BigInteger checkOrigin, List<Origin> allOrigins);

    Collection<Map<String, Object>> checkProvAndCity(List<Origin> allOrigins);

    List<Origin> getOriginByName(String searchOriginName);

    Origin getOriginTree(List<BigInteger> childrenOrigins, List<Origin> allOrigins);

    List<User> getUsersByOrigin(BigInteger originId);

    void updateOrigin(Origin origin);

    void removeUserOrigin(BigInteger userId);

    Origin getAllOriginTree();
}
