package com.datarecord.webapp.sys.origin.controller;

import com.datarecord.webapp.sys.origin.entity.Administrative;
import com.datarecord.webapp.sys.origin.service.AdministrativeService;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.webapp.support.session.SessionSupport;
import com.workbench.auth.user.entity.User;
import com.workbench.shiro.WorkbenchShiroUtils;
import com.workbench.spring.aop.annotation.JsonpCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 行政机构的新增 修改 删除 查询
 */
@Controller
@RequestMapping("administrative")
public class AdministrativeController {

    @Autowired
    private AdministrativeService administrativeService;

    /**
     * 列表查询展示
     * @param currPage
     * @param pageSize
     * @return
     */
    @RequestMapping("listAdministrative")
    @ResponseBody
    @JsonpCallback
    @CrossOrigin(allowCredentials="true")
    public String listAdministrative(int currPage, int pageSize){
        PageResult originList = administrativeService.listAdministrative(currPage, pageSize);
        String jsonpResponse = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取成功", null, originList);
        return jsonpResponse;
    }

    /**
     * 新增机构
     * @param administrative
     * @return
     */
    @RequestMapping("addAdministrative")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult addAdministrative(@RequestBody Administrative administrative){
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        administrative.setCreate_user(user.getUser_id());
        administrativeService.addAdministrative(administrative);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null,null);
        return jsonResult;
    }


    /**
     * 删除机构
     * @param organizationId
     * @return
     */
    @RequestMapping("delById")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    @Transactional
    public JsonResult deleteById(String organizationId){
        administrativeService.deleteById(organizationId);
        administrativeService.delOrganizationAndOriginAssign(organizationId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "删除成功", null,null);
        return jsonResult;
    }

    /**
     * 表user-organization-assign 保存organizationId-userId关联
     */
    @RequestMapping("userOrganizationSave")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult userOrganizationSave(Integer organizationId, Integer userId){
        administrativeService.userOrganizationSave(organizationId,userId);
        JsonResult jsonpResponse = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null, null);
        return jsonpResponse;
    }

    /**
     * 获取用户的行政机构Id
     * 通过userId获取表user-organization-assign 中的organization
     */
    @RequestMapping("getOrganizationByUser")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getOrganizationByUser(Integer userId){
        Administrative organization = administrativeService.getOrganizationByUser(userId);
        JsonResult jsonpResponse = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null, organization);
        return jsonpResponse;
    }

    /**
     * 表organization-origin-assign 保存organizationId-originId关联
     * @return
     */
    @RequestMapping("saveOrganizationAndOriginAssign")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    @Transactional
    public JsonResult saveOrganizationAndOriginAssign(String[] originIds, String organizationId){
        delOrganizationAndOriginAssign(organizationId);
        administrativeService.saveOrganizationAndOriginAssign(originIds,organizationId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null,null);
        return jsonResult;
    }

    /**
     * 删除关联 organizationId-originId
     * @return
     */
    @RequestMapping("delOrganizationAndOriginAssign")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult delOrganizationAndOriginAssign(String organizationId){
        administrativeService.delOrganizationAndOriginAssign(organizationId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null,null);
        return jsonResult;
    }

    /**
     * 获取关联 organizationId-originId
     * 通过行政机构Id获取报送机构的Ids
     * @return
     */
    @RequestMapping("getOrganizationAndOriginAssignById")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getOrganizationAndOriginAssignById(String organizationId){
        List<String> result=administrativeService.getOrganizationAndOriginAssignById(organizationId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null,result);
        return jsonResult;
    }
}
