package com.datarecord.webapp.process.controller;

import com.datarecord.enums.JobConfigStatus;
import com.datarecord.webapp.process.dao.ReportFileLog;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordMaker;
import com.datarecord.webapp.process.service.RecordProcessFlowService;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.utils.DataRecordUtil;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.auth.user.entity.UserType;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.in;

@Controller
@RequestMapping("record/flow")
public class RecordProcessFlowController {

    @Autowired
    private RecordProcessFlowService recordProcessFlowService;

    @Autowired
    private OriginService originService;

    @Autowired
    private RecordMaker recordMaker;

    @Autowired
    private RecordProcessService recordProcessService;


    @RequestMapping("pageReportDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageReportDatas(@RequestBody Map<String,Object> postParams){
        BigInteger userID = null;
        if(!DataRecordUtil.isSuperUser()){
            userID = WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id();
        }
        String currPage= postParams.get("currPage")!=null ? String.valueOf(((Double)postParams.get("currPage")).intValue()): "1";
        String pageSize= postParams.get("pageSize")!=null ? String.valueOf(((Double)postParams.get("pageSize")).intValue()): "10" ;
        Map<String,String> queryParams = (Map<String, String>) postParams.get("queryParams");
        PageResult pageData = recordProcessFlowService.pageReportDatas(userID,currPage, pageSize,queryParams);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, pageData);
        return successResult;
    }

    @RequestMapping("pageReviewDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageReviewDatas( String currPage,
                               String pageSize,
                               String reportStatus,
                               String jobId
    ){
        PageResult pageData = recordProcessFlowService.pageReviewDatas(currPage, pageSize,jobId,reportStatus);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, pageData);
        return successResult;
    }

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
        PageResult fldPageResult = recordProcessFlowService.pageReviewFlds(user.getUser_id().toString(), currPage, pageSize, queryParams);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, fldPageResult);
        return successResult;
    }

    @RequestMapping("pageReviewJobConfigs")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageReviewJobConfigs(@RequestBody JobReviewRequest jobReviewRequest){
        String currPage= jobReviewRequest.getCurrPage();
        String pageSize= jobReviewRequest.getPageSize() ;
        Map<String,String> queryParams = jobReviewRequest.getQueryParams();
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        PageResult fldPageResult = null;
        if(UserType.SYSMANAGER.compareTo(user.getUser_type())){
             fldPageResult = recordProcessFlowService.pageReviewJobConfigs(null, currPage, pageSize, queryParams);
        }else{
             fldPageResult = recordProcessFlowService.pageReviewJobConfigs(user.getUser_id().toString(), currPage, pageSize, queryParams);
        }

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, fldPageResult);
        return successResult;
    }

    @RequestMapping("authOrigins")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult authOrigins(){
//        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
//        Origin userOrigin = originService.getOriginByUser(user.getUser_id());
//        if(userOrigin==null){
//            JsonResult falidResult = JsonSupport.makeJsonpResult(
//                    JsonResult.RESULT.FAILD, "找不到当前用户所属的机构单位", "找不到当前用户所属的机构单位", "找不到当前用户所属的机构单位");
//            return falidResult;
//        }
//        List<Origin> childrenOrigins = originService.checkAllChildren(userOrigin.getOrigin_id());
//        childrenOrigins.add(0,userOrigin);
//        List<BigInteger> originIds  = new ArrayList<>();
//        for (Origin childrenOrigin : childrenOrigins) {
//            originIds.add(childrenOrigin.getOrigin_id());
//        }
//        Origin originTree = originService.getOriginTree(originIds, childrenOrigins);

        Origin originTree = originService.getAllOriginTree();

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
    public JsonResult reviewJob(@RequestBody JobFlowLog jobFlowLog){
        Integer jobId = jobFlowLog.getJob_id();
        if(jobFlowLog.getJob_flow_status()==JobConfigStatus.APPROVE.value()){
            Map<JsonResult.RESULT, Object> checkResult = recordMaker.preMake(jobId.toString());

            if(checkResult!=null&&checkResult.containsKey(JsonResult.RESULT.FAILD)){
                Object faildResult = checkResult.get(JsonResult.RESULT.FAILD);
                JsonResult successResult = JsonSupport.makeJsonpResult(
                        JsonResult.RESULT.FAILD, String.valueOf(faildResult), null, faildResult);
                return successResult;
            }
        }


        recordProcessFlowService.reviewJobItems(jobFlowLog);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "更新成功", null, JsonResult.RESULT.SUCCESS);
        return successResult;
    }

    @RequestMapping("getJobFlowLogs")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getJobFlowLogs(String jobId){
        List<JobFlowLog> jobFlowLogs = recordProcessFlowService.getJobFlowLogs(jobId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "更新成功", null, jobFlowLogs);
        return successResult;
    }
    @RequestMapping("listReportFile")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult listReportFile(String reportId){
        List<ReportFileLog> reportFileLogs = recordProcessFlowService.listReportFile(reportId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, reportFileLogs);
        return successResult;
    }

    @RequestMapping("pageJobUnitDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageJobUnitDatas(String jobId,String unitId,String currPage,String pageSize){
        PageResult pageData = recordProcessFlowService.pageJobUnitDatas(jobId, unitId,currPage,pageSize);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, pageData);
        return successResult;
    }

    @RequestMapping("pageJobDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageJobDatas(String jobId,String currPage,String pageSize){
        JobConfig jonConfig = recordProcessService.getJobConfigByJobId(jobId);
        Map unitMap = new HashMap();

        for (JobUnitConfig jobUnit : jonConfig.getJobUnits()) {
            Integer unitId = jobUnit.getJob_unit_id();
            PageResult pageData = recordProcessFlowService.pageJobUnitDatas(jobId, unitId.toString(),currPage,pageSize);
            unitMap.put(unitId,pageData);
        }

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, unitMap);
        return successResult;
    }

    @RequestMapping("getJobDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getJobDatas(String jobId){
        List<ReportJobData> reportDataList = recordProcessFlowService.getJobReportDatas(jobId,null);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, reportDataList);
        return successResult;
    }

    class JobReviewRequest{
        private String currPage;
        private String pageSize;
        private Map<String,String> queryParams;

        public String getCurrPage() {
            return currPage;
        }

        public void setCurrPage(String currPage) {
            this.currPage = currPage;
        }

        public String getPageSize() {
            return pageSize;
        }

        public void setPageSize(String pageSize) {
            this.pageSize = pageSize;
        }

        public Map<String, String> getQueryParams() {
            return queryParams;
        }

        public void setQueryParams(Map<String, String> queryParams) {
            this.queryParams = queryParams;
        }
    }

}
