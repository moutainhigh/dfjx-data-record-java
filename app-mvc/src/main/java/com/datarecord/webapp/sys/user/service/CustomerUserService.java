package com.datarecord.webapp.sys.user.service;

import com.datarecord.webapp.sys.origin.entity.ChinaAreaCode;
import com.datarecord.webapp.sys.origin.entity.OriginNature;
import com.datarecord.webapp.sys.user.entity.UserForgetPwdRecord;
import com.webapp.support.page.PageResult;

import java.util.List;
import java.util.Map;

public interface CustomerUserService {

    public PageResult pageCqnyUser(Integer currPage, Integer pageSize, String user_name, String user_type, List<Integer> originList);

    void selectOriginType(String userId, String originType);

    Map<String, String> getSmsValidateCode(Integer userId, String phone_num);

    UserForgetPwdRecord getUserForgetPwdRecord(Integer userId);

    String updateValidateCode(Integer user_id);

    String newValidateCode(Integer user_id);

    List<ChinaAreaCode> getAreaData(String parentId);

    List<OriginNature> getOriginNature();
}
