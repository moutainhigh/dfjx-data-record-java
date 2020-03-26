package com.datarecord.webapp.sys.user.service;

import com.datarecord.webapp.sys.origin.entity.ChinaAreaCode;
import com.datarecord.webapp.sys.origin.entity.OriginNature;
import com.datarecord.webapp.sys.user.entity.UserForgetPwdRecord;
import com.webapp.support.page.PageResult;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface CustomerUserService {

    public PageResult pageCqnyUser(Integer currPage, Integer pageSize, String user_name, String user_type, List<BigInteger> originList);

    void selectOriginType(String userId, String originType);

    Map<String, String> getSmsValidateCode(BigInteger userId, String phone_num);

    UserForgetPwdRecord getUserForgetPwdRecord(BigInteger userId);

    String updateValidateCode(BigInteger user_id);

    String newValidateCode(BigInteger user_id);

    List<ChinaAreaCode> getAreaData(String parentId);

    List<OriginNature> getOriginNature();
}
