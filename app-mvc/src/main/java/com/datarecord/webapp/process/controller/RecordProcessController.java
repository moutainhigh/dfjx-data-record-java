package com.datarecord.webapp.process.controller;


import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.dataindex.bean.RcdClientType;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordMaker;
import com.datarecord.webapp.process.service.RecordProcessFactory;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.google.common.base.Strings;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("record/process")
public class RecordProcessController {

    @Autowired
    private RecordMaker recordMaker;

    @RequestMapping("makeJob")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult makeJob(String jobId){
        Map<JsonResult.RESULT, Object> makeResult = recordMaker.makeJob(jobId);
        if(makeResult.containsKey(JsonResult.RESULT.FAILD)){
            JsonResult successResult = JsonSupport.makeJsonpResult(
                    JsonResult.RESULT.FAILD, JsonResult.RESULT.FAILD.toString(), null, makeResult.get(JsonResult.RESULT.FAILD));
            return successResult;
        }
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "发布流程已启动", null, JsonResult.RESULT.SUCCESS.toString());
        return successResult;
    }

    @RequestMapping("pageJob")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageJob(@RequestParam("currPage") String currPage,
                              @RequestParam("pageSize") String pageSize,
                              String reportStatus,
                              String reportName){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Map<String,String> queryParams = new HashMap<>();
        queryParams.put("reportName",reportName);
        queryParams.put("reportStatus",reportStatus);
        PageResult pageResult = RecordProcessFactory.RecordProcessSerice().pageJob(user.getUser_id(),currPage,pageSize,queryParams);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, pageResult);
        return successResult;
    }

    @RequestMapping("checkUnitStep")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult checkUnitStep(@RequestParam("reportId") String reportId){
        JobConfig jobConfig = RecordProcessFactory.RecordProcessSerice().getJobConfigByReportId(reportId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, jobConfig.getJobUnits());
        return successResult;
    }

    @RequestMapping("getFldByGroupId")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getFldByUnitId(@RequestParam("groupId") String groupId){
        List<ReportFldTypeConfig> fldConfigs = RecordProcessFactory.RecordProcessSerice().getFldByUnitId(groupId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, fldConfigs);
        return successResult;
    }

    /**
     * @param groupId
     * @param clientType 取值:PC MOBILE
     * @return
     */
    @RequestMapping("getClientFldByUnitId")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getClientFldByUnitId(
            @RequestParam("groupId") String groupId,
            @RequestParam("clientType") String clientType){
        Integer clientTypeInt = 820;
        if(!Strings.isNullOrEmpty(clientType)&&clientType.equals(RcdClientType.PC.toString())){
            clientTypeInt = RcdClientType.PC.getValue();
        }
        if(!Strings.isNullOrEmpty(clientType)&&clientType.equals(RcdClientType.MOBILE.toString())){
            clientTypeInt = RcdClientType.PC.getValue();
        }

        if(clientTypeInt==820){
            JsonResult faildResult = JsonSupport.makeJsonpResult(
                    JsonResult.RESULT.FAILD, "未找到对应的填报端", "未找到对应的填报端", null);
            return faildResult;
        }
        List<ReportFldTypeConfig> fldConfigs = RecordProcessFactory.RecordProcessSerice().getClientFldByUnitId(groupId,clientType);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, fldConfigs);
        return successResult;
    }

    @RequestMapping("getUnitDictFldContent")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getUnitDictFldContent(@RequestParam("groupId") String groupId){
        Map<Integer,List<DataDictionary>> fldDicts = RecordProcessFactory.RecordProcessSerice().getUnitDictFldContent(groupId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, fldDicts);
        return successResult;
    }


    @RequestMapping("getFldReportDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getFldReportDatas(
            @RequestParam("jobId") String jobId,
            @RequestParam("reportId") String reportId,
            @RequestParam("groupId") String groupId){
        List<ReportJobData> reportJobDatas = RecordProcessFactory.RecordProcessSerice().getFldReportDatas(jobId,reportId,groupId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, reportJobDatas);
        return successResult;
    }


    @RequestMapping("saveGridDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult saveGridDatas(@RequestBody SaveReportJobInfos reportJobInfo){
        RecordProcessFactory.RecordProcessSerice(JobUnitType.GRID).saveDatas(reportJobInfo);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "保存成功", null, JsonResult.RESULT.SUCCESS);
        return successResult;
    }

    @RequestMapping("validateGridDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult validateGridDatas(@RequestBody SaveReportJobInfos reportJobInfos){
        List<ReportJobData> newReportJobDataList = reportJobInfos.getNewReportJobInfos();
        List<ReportJobData> reportJobDataList = reportJobInfos.getReportJobInfos();
        if(!(reportJobDataList!=null&&reportJobDataList.size()>0)&&!(newReportJobDataList!=null&&newReportJobDataList.size()>0)){
            JsonResult successResult = JsonSupport.makeJsonpResult(
                    JsonResult.RESULT.FAILD, "无可校验数据", null, "无可校验数据");
            return successResult;
        }

        String unitId = null;
        if(reportJobDataList!=null&&reportJobDataList.size()>0){
            unitId = String.valueOf(reportJobDataList.get(0).getUnit_id());
        }else{
            unitId = String.valueOf(newReportJobDataList.get(0).getUnit_id());
        }
        Map<Integer, Map<Integer, String>> upValidateResult = RecordProcessFactory.RecordProcessSerice(JobUnitType.GRID).validateDatas(reportJobDataList, unitId,reportJobInfos.getClient_type());

        Map<Integer, Map<Integer, String>> newDataValidateResult = RecordProcessFactory.RecordProcessSerice(JobUnitType.GRID).validateDatas(newReportJobDataList, unitId,reportJobInfos.getClient_type());

        Map<String,Map<Integer,Map<Integer, String>>> validateResult = new HashMap<>();
        validateResult.put("reportJobDataValidate",upValidateResult);
        validateResult.put("newReportJobDataValidate",newDataValidateResult);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "校验完成", null, validateResult);

        return successResult;
    }

    @RequestMapping("saveSimpleDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult saveSimpleDatas(@RequestBody SaveReportJobInfos reportJobInfo){
        RecordProcessFactory.RecordProcessSerice(JobUnitType.SIMPLE).saveDatas(reportJobInfo);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "保存成功", null, JsonResult.RESULT.SUCCESS);
        return successResult;
    }


    @RequestMapping("validateSimpleDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult validateSimpleDatas(@RequestBody SaveReportJobInfos reportJobInfos){

        List<ReportJobData> reportJobInfoList = reportJobInfos.getReportJobInfos();

        if(!(reportJobInfoList!=null&&reportJobInfoList.size()>0)){
            JsonResult successResult = JsonSupport.makeJsonpResult(
                    JsonResult.RESULT.FAILD, "无可校验数据", null, "无可校验数据");
            return successResult;
        }

        Map<Integer, Map<Integer, String>> validateResult = RecordProcessFactory.RecordProcessSerice(JobUnitType.SIMPLE).
                validateDatas(reportJobInfoList, String.valueOf(reportJobInfoList.get(0).getUnit_id()),reportJobInfos.getClient_type());

        Map<Integer, String> responseValidate = new HashMap<>();
        if(validateResult!=null&&validateResult.size()>0){
            for (Integer key : validateResult.keySet()) {
                responseValidate = validateResult.get(key);
                break;
            }
        }

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "校验完成", null, responseValidate);
        return successResult;
    }

    @RequestMapping("doCommitAuth")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult doCommitAuth(String reportId){
        RecordProcessFactory.RecordProcessSerice().updateReportStatus(reportId, ReportStatus.SUBMIT);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "提交成功", null, null);
        return successResult;
    }

    @RequestMapping("changeStatus")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult changeStatus(String reportId,String status){
        if(Strings.isNullOrEmpty(status)){
            JsonResult faildResult = JsonSupport.makeJsonpResult(
                    JsonResult.RESULT.FAILD, "未接收到状态信息", null, null);
            return faildResult;
        }

        ReportStatus reportStatus = ReportStatus.getReportStatus(new Integer(status));

        if(reportStatus==null){
            JsonResult faildResult = JsonSupport.makeJsonpResult(
                    JsonResult.RESULT.FAILD, "未找到状态码："+status+"状态信息", null, null);
            return faildResult;
        }

        RecordProcessFactory.RecordProcessSerice().updateReportStatus(reportId, reportStatus);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "提交成功", null, JsonResult.RESULT.SUCCESS);
        return successResult;
    }
}
