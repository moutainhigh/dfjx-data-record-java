package com.datarecord.webapp.fillinatask.controller;


import com.datarecord.webapp.fillinatask.bean.Fillinatask;
import com.datarecord.webapp.fillinatask.bean.RcdJobPersonAssign;
import com.datarecord.webapp.fillinatask.bean.RcdJobUnitConfig;
import com.datarecord.webapp.fillinatask.service.FillinataskService;
import com.github.pagehelper.Page;
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
 * 填报任务Controller
 */
@Controller
@RequestMapping("/fillinatask")
public class FillinataskController {

    @Autowired
    private FillinataskService fillinataskService;


    //填报任务列表
    @RequestMapping("/rcdjobconfiglist")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String rcdjobconfiglist(
            @RequestParam("currPage") int currPage,
            @RequestParam("pageSize")int pageSize,
            @RequestParam("job_name")String job_name,
            @RequestParam("job_status")String job_status
    ){
        PageResult pageResult = null;
        String jsonResult = "";
        try{
            pageResult = fillinataskService.rcdjobconfiglist(currPage,pageSize,job_name,job_status);
        }catch(Exception e){
            e.printStackTrace();
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取填报任务列表失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取填报任务列表成功", null, pageResult);
        return jsonResult;
    }

    //填报任务删除   包含任务下的任务组、任务与关联关系等关联关系
    @RequestMapping("/deletercdjobconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercdjobconfig(
            @RequestParam("job_id")String job_id
    ){
        String jsonResult = "";
        try{
            fillinataskService.deletercdjobconfig(job_id);    //填报任务删除
            fillinataskService.deletercdjobpersonassign(job_id);    //填报人维护删除
            fillinataskService.deleteRcdJobUnitConfigsuo(job_id);    //任务关连填报组删除
        }catch(Exception e){
            e.printStackTrace();
            return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报任务删除失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报任务删除成功", null, "success");
        return jsonResult;
    }



    //任务绑定的填报人删除
    @RequestMapping("/deletercdjobpersonassignbyuseridandjobid")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercdjobpersonassignbyuseridandjobid(
            @RequestParam("job_id")String job_id,
            @RequestParam("user_id")String user_id
    ){
        String jsonResult = "";
        try{
            fillinataskService.deletercdjobpersonassignbyuseridandjobid(job_id,user_id);
        }catch(Exception e){
            e.printStackTrace();
            return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "任务绑定的填报人删除失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "任务绑定的填报人删除成功", null, "success");
        return jsonResult;
    }

    //任务id查看详情
    @RequestMapping("/selectrcdjobconfigjobid")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectrcdjobconfigjobid(
            @RequestParam("job_id")String job_id
    ){
        String jsonResult = "";
        List<Fillinatask> list = new ArrayList<Fillinatask>();
        try{
            list = fillinataskService.selectrcdjobconfigjobid(job_id);
        }catch(Exception e){
            e.printStackTrace();
            return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "任务id查看详情失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "任务id查看详情成功", null, list);
        return jsonResult;
    }





    //新增任务
    @RequestMapping("/insertrcdjobconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcdjobconfig(
            @RequestParam("job_name")String job_name,
            @RequestParam("job_start_dt")String job_start_dt,
            @RequestParam("job_end_dt")String job_end_dt
    ){
        String jsonResult = "";
        try{
         fillinataskService.insertrcdjobconfig(job_name,job_start_dt,job_end_dt);
        }catch(Exception e){
            e.printStackTrace();
            return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增填报任务失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增填报任务成功", null, "success");
        return jsonResult;
    }



    //修改任务
    @RequestMapping("/updatercdjobconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatercdjobconfig(
            @RequestParam("job_id")String job_id,
            @RequestParam("job_name")String job_name,
            @RequestParam("job_start_dt")String job_start_dt,
            @RequestParam("job_end_dt")String job_end_dt
    ){
        String jsonResult = "";
        try{
            fillinataskService.updatercdjobconfig(job_id,job_name,job_start_dt,job_end_dt);
        }catch(Exception e){
            e.printStackTrace();
            return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改填报任务失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改填报任务成功", null, "success");
        return jsonResult;
    }

    //填报任务中 填报人维护rcd_job_person_assign
    @RequestMapping("/insertrcdjobpersonassign")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcdjobpersonassign(
            @RequestParam("userid")String userid,
            @RequestParam("job_id")String job_id
    ){
        String jsonResult = "";
        try{
          //  fillinataskService.deletercdjobpersonassign(job_id);    //把原先的删除再新增
            fillinataskService.insertrcdjobpersonassign(job_id,userid);
        }catch(Exception e){
            e.printStackTrace();
            return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增填报维护人失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增填报维护人成功", null, "success");
        return jsonResult;
    }


  //查询机构对应填报人
    @RequestMapping("/huixianrcdjobpersonassign")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String huixianrcdjobpersonassign(
            @RequestParam("job_id")String job_id
    ){
        List<RcdJobPersonAssign> ll = new ArrayList<RcdJobPersonAssign>();
        String jsonResult = "";
        try{
            ll =   fillinataskService.huixianrcdjobpersonassign(job_id);
        }catch(Exception e){
            e.printStackTrace();
            return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "查询机构对应填报人失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "查询机构对应填报人成功", null, ll);
        return jsonResult;
    }



    //填报组维护弹框RcdJobUnitConfig  待选择列表
    @RequestMapping("/selectRcdJobUnitConfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectRcdJobUnitConfig(
            @RequestParam("job_id")String job_id
    ){
        List<RcdJobUnitConfig> ll = new ArrayList<RcdJobUnitConfig>();
        String jsonResult = "";
        try{
            ll =  fillinataskService.selectRcdJobUnitConfig(job_id);
        }catch(Exception e){
            e.printStackTrace();
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报组查询未选择失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报组查询未选择成功", null, ll);
        return jsonResult;
    }

    //填报组维护弹框RcdJobUnitConfig  已选择列表
    @RequestMapping("/selectRcdJobUnitConfigyi")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectRcdJobUnitConfigyi(
            @RequestParam("job_id")String job_id
    ){
        List<RcdJobUnitConfig> ll = new ArrayList<RcdJobUnitConfig>();
        String jsonResult = "";
        try{
            ll =  fillinataskService.selectRcdJobUnitConfigyi(job_id);
        }catch(Exception e){
            e.printStackTrace();
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报组查询已选择失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报组查询已选择成功", null, ll);
        return jsonResult;
    }


    //弹框确认
    @RequestMapping("/updateRcdJobUnitConfigyi")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updateRcdJobUnitConfigyi(
            @RequestParam("jobunitid")String jobunitid,
            @RequestParam("job_id")String job_id
    ){
        String jsonResult = "";
        try{
              fillinataskService.updateRcdJobUnitConfigsuo(job_id);
              fillinataskService.updateRcdJobUnitConfigyi(jobunitid,job_id);
        }catch(Exception e){
            e.printStackTrace();
            return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报组维护弹框确认失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报组维护弹框确认成功", null, "success");
        return jsonResult;
    }








}
