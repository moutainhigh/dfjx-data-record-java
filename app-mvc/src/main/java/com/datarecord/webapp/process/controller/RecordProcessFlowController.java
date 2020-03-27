package com.datarecord.webapp.process.controller;

import com.datarecord.enums.JobConfigStatus;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobFlowLog;
import com.datarecord.webapp.process.service.RecordMaker;
import com.datarecord.webapp.process.service.RecordProcessFlowService;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
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

    @Autowired
    private RecordMaker recordMaker;

    @Autowired
    private RecordProcessService recordProcessService;

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
        PageResult fldPageResult = recordProcessFlowService.pageReviewJobConfigs(user.getUser_id().toString(), currPage, pageSize, queryParams);

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
        JobConfig jobCOnfig = recordProcessService.getJobConfigByJobId(jobId.toString());
        if(jobFlowLog.getJob_flow_status()==JobConfigStatus.APPROVE.value()){
            Map<JsonResult.RESULT, Object> checkResult = recordMaker.preMake(jobCOnfig);

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
