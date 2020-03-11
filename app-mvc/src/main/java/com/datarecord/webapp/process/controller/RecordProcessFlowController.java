package com.datarecord.webapp.process.controller;

import com.datarecord.webapp.process.service.RecordProcessFactory;
import com.datarecord.webapp.process.service.RecordProcessFlowService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.sys.origin.tree.TreeUtil;
import com.google.common.base.Strings;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
    @RequestMapping("record/flow")
public class RecordProcessFlowController {

    @Autowired
    private RecordProcessFlowService recordProcessFlowService;

    @Autowired
    private OriginService originService;

    @RequestMapping("pageJob")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageJob( String currPage,
                               String pageSize,
                               String reportStatus,
                               String reportOrigin,
                               String reportName
    ){
        PageResult pageData = recordProcessFlowService.pageJob(currPage, pageSize,reportStatus,reportName,reportOrigin);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, pageData);
        return successResult;
    }


    @RequestMapping("pageReviewFlds")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageReviewFlds(@RequestBody Map<String,Object> postParams){
        String currPage= postParams.get("currPage")!=null ? String.valueOf(((Double)postParams.get("currPage")).intValue()): "1";
        String pageSize= postParams.get("pageSize")!=null ? String.valueOf(((Double)postParams.get("pageSize")).intValue()): "10" ;
        Map<String,String> queryParams = (Map<String, String>) postParams.get("queryParams");
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        PageResult fldPageResult = recordProcessFlowService.pageReviewFlds(user.getUser_id(), currPage, pageSize, queryParams);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, fldPageResult);
        return successResult;
    }

    @RequestMapping("pageReviewJobConfigs")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageReviewJobConfigs(@RequestBody Map<String,Object> postParams){
        String currPage= postParams.get("currPage")!=null ? String.valueOf(((Double)postParams.get("currPage")).intValue()): "1";
        String pageSize= postParams.get("pageSize")!=null ? String.valueOf(((Double)postParams.get("pageSize")).intValue()): "10" ;
        Map<String,String> queryParams = (Map<String, String>) postParams.get("queryParams");
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        PageResult fldPageResult = recordProcessFlowService.pageReviewJobConfigs(user.getUser_id(), currPage, pageSize, queryParams);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, fldPageResult);
        return successResult;
    }

    @RequestMapping("authOrigins")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult authOrigins(){
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        Origin userOrigin = originService.getOriginByUser(user.getUser_id());
        List<Origin> childrenOrigins = originService.checkAllChildren(userOrigin.getOrigin_id());
        childrenOrigins.add(0,userOrigin);
        List<Integer> originIds  = new ArrayList<>();
        for (Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id());
        }
        Origin originTree = originService.getOriginTree(originIds, childrenOrigins);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, originTree);
        return successResult;
    }

    @RequestMapping("subJobConfig")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult subJobConfig(String jobId){
        recordProcessFlowService.subJobConfig(jobId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "更新成功", null, JsonResult.RESULT.SUCCESS);
        return successResult;
    }

    @RequestMapping("reviewFld")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult reviewFld(String fldId,String status){
        recordProcessFlowService.reviewFld(fldId,status);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "更新成功", null, JsonResult.RESULT.SUCCESS);
        return successResult;
    }


    @RequestMapping("reviewJob")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult reviewJob(String jobId,String status){
        recordProcessFlowService.reviewJobItems(jobId,status);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "更新成功", null, JsonResult.RESULT.SUCCESS);
        return successResult;
    }
}
