package com.datarecord.webapp.sys.origin.service.impl;

import com.datarecord.webapp.sys.origin.dao.IAdministrativeDao;
import com.datarecord.webapp.sys.origin.entity.Administrative;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.AdministrativeService;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service("administrative")
public class AdministrativeServiceImp implements AdministrativeService {

    @Autowired
    private IAdministrativeDao administrativeDao;


    @Override
    public PageResult listAdministrative(int currPage, int pageSize) {
        Page<Administrative> administrativePage = administrativeDao.listAdministrative(currPage, pageSize);
        PageResult pageResult = PageResult.pageHelperList2PageResult(administrativePage);
        return pageResult;
    }

    @Override
    public void addAdministrative(Administrative administrative) {
        if(administrative.getOrganization_id()!=null){
            administrativeDao.updateAdministrative(administrative);
        }else{
            administrativeDao.addAdministrative(administrative);
        }
    }

    @Override
    public void deleteById(String originId) {
        administrativeDao.deleteById(originId);
    }

    @Override
    public void userOrganizationSave(Integer organizationId, Integer userId) {
        administrativeDao.removeUserOrganization(userId);
        administrativeDao.userOrganizationSave(organizationId,userId);
    }

    @Override
    public Administrative getOrganizationByUser(Integer userId) {
        return administrativeDao.getOrganizationByUser(userId);
    }

    @Override
    public void saveOrganizationAndOriginAssign(String[] originIds, String organizationId) {
        administrativeDao.saveOrganizationAndOriginAssign(originIds,organizationId);
    }

    @Override
    public List<String> getOrganizationAndOriginAssignById(String organizationId) {
        return administrativeDao.getOrganizationAndOriginAssignById(organizationId);
    }

    @Override
    public void delOrganizationAndOriginAssign(String organizationId) {
        administrativeDao.delOrganizationAndOriginAssign(organizationId);
    }

    @Override
    public List<Origin> listAllOriginForOrganization(Integer userId){
        return administrativeDao.listAllOriginForOrganization(userId);
    }
}
