package com.datarecord.webapp.reportinggroup.controller;

import com.datarecord.webapp.reportinggroup.bean.RcdJobUnitFld;
import com.datarecord.webapp.reportinggroup.bean.rcdJobConfig;
import com.datarecord.webapp.reportinggroup.service.ReportingGroupService;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 填报组Controller
 */
@Controller
@RequestMapping("/reporting")
public class ReportingGroupController {

    @Autowired
    private ReportingGroupService reportingGroupService;

   //left填报任务rcd_job_config
   @RequestMapping("/leftrcdjobconfig")
   @ResponseBody
   @CrossOrigin(allowCredentials="true")
   public String leftrcdjobconfig(){
       List<rcdJobConfig> ll = new ArrayList<rcdJobConfig>();
       String jsonResult = "";
       try{
           ll  = reportingGroupService.leftrcdjobconfig();
       }catch(Exception e){
           return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "left填报任务获取失败", null, "error");
       }
       return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "left填报任务获取成功", null, ll);
   }


    //填报组列表
    @RequestMapping("/rcdjobunitconfiglist")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String rcdjobunitconfiglist(
            @RequestParam("currPage") int currPage,
            @RequestParam("pageSize")int pageSize,
            @RequestParam("job_id")String job_id     //任务编号 任务编号
    ){
        PageResult pageResult = null;
        String jsonResult = "";
        try{
            pageResult = reportingGroupService.rcdjobunitconfiglist(currPage,pageSize,job_id);
        }catch(Exception e){
            jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取填报组列表失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取填报组列表成功", null, pageResult);
        return jsonResult;
    }

    //填报组新增
    @RequestMapping("/insertrcdjobunitconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcdjobunitconfig(
            @RequestParam("job_id")String job_id,
            @RequestParam("job_unit_name")String job_unit_name,
            @RequestParam("job_unit_active")String job_unit_active
    ){
        String jsonResult = "";
        try{
            reportingGroupService.insertrcdjobunitconfig(job_id,job_unit_name,job_unit_active);
        }catch(Exception e){
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除失败", null, "error");
        }
        return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除成功", null, "success");
    }

    //填报组删除
    @RequestMapping("/deletercdjobunitconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercdjobunitconfig(
            @RequestParam("job_unit_id")String job_unit_id){
        String jsonResult = "";
       try{
          reportingGroupService.deletercdjobunitconfig(job_unit_id);
        }catch(Exception e){
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除失败", null, "error");
        }
        return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除成功", null, "success");
    }


    //点击指标查出所关联关系
    @RequestMapping("/selectrcdjobunitfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectrcdjobunitfld(
            @RequestParam("job_unit_id")String job_unit_id){
        List<RcdJobUnitFld> ll = new ArrayList<RcdJobUnitFld>();
        String jsonResult = "";
        try{
            ll  = reportingGroupService.selectrcdjobunitfld(job_unit_id);   //查出有关联的指标id
        }catch(Exception e){
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "left填报任务获取失败", null, "error");
        }
        return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "left填报任务获取成功", null, ll);
    }


    //填报组与指标关联关系维护
    @RequestMapping("/rcdjobunitfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String rcdjobunitfld(
            @RequestParam("jobunitid")String jobunitid,       //编码
            @RequestParam("fld_id")String fld_id  //指标编码数组
            ){
        String jsonResult = "";
        try{
            reportingGroupService.rcdjobunitflddelete(jobunitid);  //删除之前关系
            reportingGroupService.rcdjobunitfld(fld_id,jobunitid);  //新增关系
        }catch(Exception e){
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增填报组与指标关联关系维护失败", null, "error");
        }
        return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增填报组与指标关联关系维护成功", null, "success");
    }










}
