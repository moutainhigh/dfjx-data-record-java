package com.datarecord.webapp.rcduser.controller;

import com.datarecord.enums.JobConfigStatus;
import com.datarecord.webapp.fillinatask.service.JobConfigService;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.rcduser.bean.RecordUser;
import com.datarecord.webapp.rcduser.bean.RecordUserGroup;
import com.datarecord.webapp.rcduser.service.RecordUserService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.utils.DataRecordUtil;
import com.google.common.base.Strings;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 填报人Controller
 */
@Controller
@RequestMapping("/recordUser")
public class RecordUserController {
    @Autowired
    private RecordUserService recordUserService;

    @Autowired
    private JobConfigService jobConfigService;

    @RequestMapping("pageUserGroup")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult pageRecordUserGroup(String currPage,String pageSize){
        PageResult pageResult = recordUserService.pageRecordUserGroup(currPage,pageSize);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null, pageResult);
        return jsonResult;
    }


    @RequestMapping("saveUserGroup")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult saveUserGroup(String groupName){
        recordUserService.saveUserGroup(groupName);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "添加成功", null, null);
        return jsonResult;
    }

    @RequestMapping("updateUserGroup")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult updateUserGroup(@RequestBody RecordUserGroup recordUserGroup){
        recordUserService.updateUserGroup(recordUserGroup);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "添加成功", null, null);
        return jsonResult;
    }

    @RequestMapping("delUserGroup")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult delUserGroup(String groupId){
        recordUserService.delUserGroup(groupId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "删除成功", null, null);
        return jsonResult;
    }

    @RequestMapping("activeUserGroup")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult activeUserGroup(String groupId){
        recordUserService.activeUserGroup(groupId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "添加成功", null, null);
        return jsonResult;
    }

    @RequestMapping("getActiveUserGroup")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getActiveUserGroup(){
        RecordUserGroup recordUserGroup = recordUserService.getActiveUserGroup();
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null, recordUserGroup);
        return jsonResult;
    }

    @RequestMapping("getActiveGpOrgs")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getActiveGpOrgs(){
        RecordUserGroup recordUserGroup = recordUserService.getActiveUserGroup();
        if(recordUserGroup!=null){
            List<Origin> origins = recordUserService.groupOrigins(recordUserGroup.getGroup_id().toString());
            JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null, origins);
            return jsonResult;
        }
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "获取失败", "未找到默认用户组", null);
        return jsonResult;
    }

    @RequestMapping("groupUsers")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    /**
     * 当前填报分组下所有用户
     */
    public JsonResult groupUsers(String groupId){
        List<User> groupUsers = recordUserService.groupUsers(groupId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null, groupUsers);
        return jsonResult;
    }

    @RequestMapping("groupOriginIds")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult groupOriginIds(String groupId){
        List<String> groupOriginIds = recordUserService.groupOriginIds(groupId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null, groupOriginIds);
        return jsonResult;
    }

    @RequestMapping("groupOrigins")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult groupOrigins(String groupId){
        List<Origin> groupOrigins = recordUserService.groupOrigins(groupId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null, groupOrigins);
        return jsonResult;
    }

    @RequestMapping("groupOriginUsers")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    /**
     * 当前填报分组下所选机构下所有用户 包括关联了和未关联
     */
    public JsonResult groupOriginUsers(@RequestBody OriginGroupRq originGroupRq){
        List<RecordUser> groupOriginUsers = new ArrayList<>();
        if(originGroupRq.originIds!=null&&originGroupRq.originIds.size()>0){
            groupOriginUsers = recordUserService.groupOriginUsers(originGroupRq.groupId,originGroupRq.originIds);
        }
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null, groupOriginUsers);
        return jsonResult;
    }

    //新增确定
    @RequestMapping("/addUserToGroup")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String addUserToGroup(@RequestBody RecordUser recordUser){
        recordUserService.addUserToGroup(recordUser);   //新增
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报人新增成功", null, "success");
    }

    @RequestMapping("/delUserFromGroup")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String delUserFromGroup(@RequestBody RecordUser recordUser){
        recordUserService.delUserFromGroup(recordUser);
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报人删除成功", null, "success");
    }

    @RequestMapping("unCheckOriginUser")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult unCheckOriginUser(
            String currPage,
            String pageSize,
            String jobId,
            String originId){
        PageResult recordUserPage = recordUserService.unCheckOriginUser(currPage,pageSize,jobId,originId);
        return JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "删除成功", null, recordUserPage);
    }

    @RequestMapping("checkedOriginUser")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult checkedOriginUser(
            String currPage,
            String pageSize,
            String jobId,
            String originId){
        PageResult recordUserPage = recordUserService.checkedOriginUser(currPage,pageSize,jobId,originId);
        return JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "删除成功", null, recordUserPage);
    }


    @RequestMapping("jobUserGroupHis")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult jobUserGroupHis(String jobId){
        List<RecordUserGroup> recordUserGroup = recordUserService.jobUserGroupHis(jobId);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, recordUserGroup);
        return successResult;
    }

    @RequestMapping("getJobOriginHis")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getJobOriginHis(String jobId){
        List<Origin> resultOrigins = recordUserService.getJobOriginHis(jobId);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, resultOrigins);
        return successResult;
    }

    @RequestMapping("getJobOrigins")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getJobOrigins(String jobId){
        List<Origin> resultOrigins = null;
        if(Strings.isNullOrEmpty(jobId)){
            List<RecordUserGroup> userGroupList = recordUserService.getUserGroupList();
            if(userGroupList!=null&&userGroupList.size()>0){
                for (RecordUserGroup recordUserGroup : userGroupList) {
                    if(resultOrigins==null)
                        resultOrigins = recordUserService.groupOrigins(recordUserGroup.getGroup_id().toString());
                    else
                        resultOrigins.addAll(recordUserService.groupOrigins(recordUserGroup.getGroup_id().toString()));
                }
            }
        }else{
            JobConfig jobConfig = jobConfigService.getJobConfig(jobId);
            Integer jobConfigStatus = jobConfig.getJob_status();
            if(JobConfigStatus.SUBMIT.compareWith(jobConfigStatus)||
                    JobConfigStatus.SUBMITING.compareWith(jobConfigStatus)){
                resultOrigins = recordUserService.getJobOriginHis(jobId);
            }else {

                List<RecordUserGroup> userGroupList = recordUserService.getUserGroupList();
                if(userGroupList!=null&&userGroupList.size()>0){
                    for (RecordUserGroup recordUserGroup : userGroupList) {
                        if(resultOrigins==null)
                            resultOrigins = recordUserService.groupOrigins(recordUserGroup.getGroup_id().toString());
                        else
                            resultOrigins.addAll(recordUserService.groupOrigins(recordUserGroup.getGroup_id().toString()));
                    }
                }

//                RecordUserGroup activeUserGroup = recordUserService.getActiveUserGroup();
//                resultOrigins = recordUserService.groupOrigins(activeUserGroup.getGroup_id().toString());
            }
        }

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, resultOrigins);
        return successResult;
    }

    @RequestMapping("isSupperUser")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult isSupperUser(){
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, DataRecordUtil.isSuperUser());
        return successResult;
    }


    @RequestMapping("getUserGroupList")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getUserGroupList(){
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, recordUserService.getUserGroupList());
        return successResult;
    }

    class OriginGroupRq{
        String groupId;
        List<String> originIds;
    }
}
