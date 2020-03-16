package com.datarecord.webapp.reportinggroup.controller;

import com.datarecord.webapp.reportinggroup.bean.RcdJobUnitFld;
import com.datarecord.webapp.reportinggroup.bean.ReportingGroup;
import com.datarecord.webapp.reportinggroup.bean.rcdJobConfig;
import com.datarecord.webapp.reportinggroup.service.ReportingGroupService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.github.pagehelper.Page;
import com.webapp.support.httpClient.HttpClientSupport;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private OriginService originService;

   //left填报任务rcd_job_config
   @RequestMapping("/leftrcdjobconfig")
   @ResponseBody
   @CrossOrigin(allowCredentials="true")
   public String leftrcdjobconfig(){
       List<rcdJobConfig> ll = null;

       try{
           User user = WorkbenchShiroUtils.checkUserFromShiroContext();
           Origin userOrigin = originService.getOriginByUser(user.getUser_id());
           ll  = reportingGroupService.leftrcdjobconfig(userOrigin);
       }catch(Exception e){
           e.printStackTrace();
           return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "left填报任务获取失败", null, "error");
       }
       return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "left填报任务获取成功", null, ll);
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

        try{
            pageResult = reportingGroupService.rcdjobunitconfiglist(currPage,pageSize,job_id);
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取填报组列表失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取填报组列表成功", null, pageResult);
    }

    //填报组新增
    @RequestMapping("/insertrcdjobunitconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcdjobunitconfig(@RequestBody ReportingGroup reportingGroup
    ){
        try{
            reportingGroupService.insertrcdjobunitconfig(reportingGroup);
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除失败", null, "error");
        }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除成功", null, "success");
    }


    //填报组回显
    @RequestMapping("/selectrcdjobunitconfigByjobunitid")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectrcdjobunitconfigByjobunitid(
            @RequestParam("job_unit_id")String job_unit_id
    ){
        ReportingGroup reportingGroup = null;
        try{
            reportingGroup = reportingGroupService.selectrcdjobunitconfigByjobunitid(job_unit_id);
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报组回显失败", null, "error");
        }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报组回显成功", null, reportingGroup);
    }


    //填报组修改
    @RequestMapping("/updatercdjobunitconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
       public String updatercdjobunitconfig(@RequestBody ReportingGroup reportingGroup){
        try{
            reportingGroupService.updatercdjobunitconfig(reportingGroup);
        }catch(Exception e){
            e.printStackTrace();
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报组修改失败", null, "error");
        }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报组修改成功", null, "success");
    }

    //填报组删除
    @RequestMapping("/deletercdjobunitconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercdjobunitconfig(
            @RequestParam("job_unit_id")String job_unit_id){

       try{
          reportingGroupService.deletercdjobunitconfig(job_unit_id);
        }catch(Exception e){
           e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除失败", null, "error");
        }
        return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除成功", null, "success");
    }


    //点击指标查出所关联关系
    @RequestMapping("/selectrcdjobunitfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectrcdjobunitfld(
            @RequestParam("job_unit_id")String job_unit_id){
        List<RcdJobUnitFld> ll = null;
        try{
            User user = WorkbenchShiroUtils.checkUserFromShiroContext();
            Origin userOrigin = originService.getOriginByUser(user.getUser_id());
            ll  = reportingGroupService.selectrcdjobunitfld(userOrigin,job_unit_id);   //查出有关联的指标id
        }catch(Exception e){
            e.printStackTrace();
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "指标获取失败", null, "error");
        }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "指标获取成功", null, ll);
    }


    //填报组与指标关联关系维护
    @RequestMapping("/rcdjobunitfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String rcdjobunitfld(
            @RequestParam("jobunitid")String jobunitid,       //编码
            @RequestParam("fld_id")String fld_id  //指标编码数组
            ){
        try{
            reportingGroupService.rcdjobunitfld(fld_id,jobunitid);  //新增关系
        }catch(Exception e){
            e.printStackTrace();
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增填报组与指标关联关系维护失败", null, "error");
        }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增填报组与指标关联关系维护成功", null, "success");
    }










}
