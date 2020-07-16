package com.datarecord.webapp.notice.controller;

import com.datarecord.webapp.notice.entity.ReportSmsConfig;
import com.datarecord.webapp.notice.service.ReportSmsService;
import com.google.common.base.Strings;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("sms")
public class ReportSmsController {

    @Autowired
    private ReportSmsService reportSmsService;

    @RequestMapping("pagerSms")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult pagerSms(Integer currPage, Integer pageSize){

        PageResult pagerData = reportSmsService.pagerSms(currPage,pageSize);

        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null,pagerData);
        return jsonResult;
    }

    @RequestMapping("getSmsTemplates")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getSmsTemplates(){
        List<Map<String,Object>> aliSmsTemplateList = reportSmsService.getAliSmsTemplates();
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null,aliSmsTemplateList);
        return jsonResult;
    }


    @RequestMapping("createSmsJob")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult createSmsJob(@RequestBody ReportSmsConfig reportSMsConfig ){
        String faildReson = reportSmsService.createSmsJob(reportSMsConfig);
        if(Strings.isNullOrEmpty(faildReson)){
            JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null,JsonResult.RESULT.SUCCESS);
            return jsonResult;
        }else{
            JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, faildReson, faildReson,faildReson);
            return jsonResult;
        }

    }

    @RequestMapping("updateSmsJob")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult updateSmsJob(@RequestBody ReportSmsConfig reportSMsConfig ){
        String faildReson = reportSmsService.updateSmsJob(reportSMsConfig);
        if(Strings.isNullOrEmpty(faildReson)){
            JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null,JsonResult.RESULT.SUCCESS);
            return jsonResult;
        }else{
            JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, faildReson, faildReson,faildReson);
            return jsonResult;
        }
    }

    @RequestMapping("getSmsJob")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getSmsJob(String id){
        ReportSmsConfig reportSmsConfig = reportSmsService.getSmsJob(id);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null,reportSmsConfig);
        return jsonResult;
    }

    @RequestMapping("deleteSmsJob")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult createSmsJob(String smsId){
        reportSmsService.deleteSmsJob(smsId);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "删除成功", null,JsonResult.RESULT.SUCCESS);
        return jsonResult;
    }

    @RequestMapping("updateConfigStatus")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult updateConfigStatus(String smsId,String config_status){
        reportSmsService.updateConfigStatus(smsId,config_status);
        JsonResult jsonResult = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "删除成功", null,JsonResult.RESULT.SUCCESS);
        return jsonResult;
    }
}
