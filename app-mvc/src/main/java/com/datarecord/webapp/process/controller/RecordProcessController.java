package com.datarecord.webapp.process.controller;


import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("record/process")
public class RecordProcessController {

    @Autowired
    private RecordProcessService recordProcessService;

    @RequestMapping("makeJob")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult makeJob(String jobId){
        recordProcessService.makeJob(jobId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "发布流程已启动", null, JsonResult.RESULT.SUCCESS.toString());
        return successResult;
    }

    @RequestMapping("pageJob")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageJob(@RequestParam("currPage") String currPage,@RequestParam("pageSize") String pageSize){
        User user = (User) SecurityUtils.getSubject().getPrincipal();

        PageResult pageResult = recordProcessService.pageJob(user.getUser_id(),currPage,pageSize);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, pageResult);
        return successResult;
    }

    @RequestMapping("checkUnitStep")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult checkUnitStep(@RequestParam("reportId") String reportId){
        JobConfig jobConfig = recordProcessService.getJobConfigByReportId(reportId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, jobConfig.getJobUnits());
        return successResult;
    }

    @RequestMapping("getFldByGroupId")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getFldByUnitId(@RequestParam("groupId") String groupId){
        List<ReportFldConfig> jobConfig = recordProcessService.getFldByUnitId(groupId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, jobConfig);
        return successResult;
    }
    @RequestMapping("getUnitDictFldContent")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getUnitDictFldContent(@RequestParam("groupId") String groupId){
        Map<Integer,List<DataDictionary>> fldDicts = recordProcessService.getUnitDictFldContent(groupId);
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
        List<ReportJobData> reportJobDatas = recordProcessService.getFldReportDatas(jobId,reportId,groupId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, reportJobDatas);
        return successResult;
    }


    @RequestMapping("saveGridDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult saveGridDatas(@RequestBody ReportJobInfo reportJobInfo){
        recordProcessService.saveGridDatas(reportJobInfo);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "保存成功", null, JsonResult.RESULT.SUCCESS);
        return successResult;
    }

    @RequestMapping("validateGridDatas")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult validateGridDatas(@RequestBody ReportJobInfo reportJobInfo){
        List<ReportJobData> reportJobDataList = reportJobInfo.getReportJobDataList();
        String unitId = null;
        if(reportJobDataList!=null&&reportJobDataList.size()>0){
            unitId = String.valueOf(reportJobDataList.get(0).getUnit_id());
        }
        Map<Integer, Map<Integer, String>> validateResult = recordProcessService.validateGridDatas(reportJobDataList, unitId);
        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "校验完成", null, validateResult);
        return successResult;
    }


}
