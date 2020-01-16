package com.datarecord.webapp.process.controller;


import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.datarecord.webapp.process.entity.ReportJobData;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
                JsonResult.RESULT.SUCCESS, "发布流程已驱动", null, JsonResult.RESULT.SUCCESS.toString());
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


}
