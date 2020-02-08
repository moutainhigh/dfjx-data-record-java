package com.datarecord.webapp.sys.origin.service;

import com.datarecord.webapp.sys.origin.entity.Administrative;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.webapp.support.page.PageResult;

import java.util.List;


public interface AdministrativeService {

    PageResult listAdministrative(int currPage, int pageSize);

    void addAdministrative(Administrative administrative);

    void deleteById(String originId);

    void userOrganizationSave(Integer organizationId, Integer userId);

    Administrative getOrganizationByUser(Integer userId);

    void saveOrganizationAndOriginAssign(String[] originIds, String organizationId);

    List<String> getOrganizationAndOriginAssignById(String organizationId);

    void delOrganizationAndOriginAssign(String organizationId);

    List<Origin> listAllOriginForOrganization(Integer userId);
}
