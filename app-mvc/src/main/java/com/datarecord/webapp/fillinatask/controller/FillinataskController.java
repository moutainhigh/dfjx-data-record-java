package com.datarecord.webapp.fillinatask.controller;


import com.datarecord.webapp.fillinatask.bean.Fillinatask;
import com.datarecord.webapp.fillinatask.bean.JobInteval;
import com.datarecord.webapp.fillinatask.bean.RcdJobPersonAssign;
import com.datarecord.webapp.fillinatask.bean.RcdJobUnitConfig;
import com.datarecord.webapp.fillinatask.service.FillinataskService;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.auth.user.entity.UserType;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 填报任务Controller
 */
@Controller
@RequestMapping("/fillinatask")
public class FillinataskController {

    @Autowired
    private FillinataskService fillinataskService;

    @Autowired
    private OriginService originService;

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
        try{
            User user = WorkbenchShiroUtils.checkUserFromShiroContext();
            if(UserType.SYSMANAGER.compareTo(user.getUser_type())){
                pageResult = fillinataskService.rcdjobconfiglist(currPage,pageSize,job_name,job_status,null);
            }else{
                pageResult = fillinataskService.rcdjobconfiglist(currPage,pageSize,job_name,job_status,String.valueOf(user.getUser_id()));
            }
            //  Origin userOrigin = originService.getOriginByUser(user.getUser_id());
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取填报任务列表失败", null, "error");
        }
        return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取填报任务列表成功", null, pageResult);
    }

    //填报任务删除   包含任务下的任务组、任务与关联关系等关联关系       （ 物理删除）
    @RequestMapping("/deletercdjobconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercdjobconfig(
            @RequestParam("job_id")String job_id
    ){
        try{
            fillinataskService.deletercdjobconfig(job_id);    //填报任务删除
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报任务删除失败", null, "error");
        }
        return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报任务删除成功", null, "success");
    }


    //填报任务删除   包含任务下的任务组、任务与关联关系等关联关系        （ 软删除）
    @RequestMapping("/updateByIdConfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updateByIdConfig(
            @RequestParam("job_id")String job_id
    ){
        try{
            fillinataskService.updateByIdConfig(job_id);    //填报任务软删除
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报任务软删除失败", null, "error");
        }
        return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报任务软删除成功", null, "success");
    }



    //任务绑定的填报人删除
    @RequestMapping("/deletercdjobpersonassignbyuseridandjobid")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercdjobpersonassignbyuseridandjobid(
            @RequestParam("job_id")String job_id,
            @RequestParam("user_id")String user_id
    ){
        try{
            fillinataskService.deletercdjobpersonassignbyuseridandjobid(job_id,user_id);
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "任务绑定的填报人删除失败", null, "error");
        }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "任务绑定的填报人删除成功", null, "success");
    }

    //任务id查看详情
    @RequestMapping("/selectrcdjobconfigjobid")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectrcdjobconfigjobid(
            @RequestParam("job_id")String job_id
    ){
        JobConfig jobConfig = null;
        try{
            jobConfig = fillinataskService.selectrcdjobconfigjobid(job_id);
            List<JobInteval> jobIntevals = fillinataskService.getJobIntevals(job_id);
            jobConfig.setJob_intervals(jobIntevals);
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "任务id查看详情失败", null, "error");
        }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "任务id查看详情成功", null, jobConfig);
    }





    //新增任务
    @RequestMapping("/insertrcdjobconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String saveJobConfig(@RequestBody JobConfig jobConfig){
        try{
            User user = WorkbenchShiroUtils.checkUserFromShiroContext();
            Origin userOrigin = originService.getOriginByUser(user.getUser_id());
            BigInteger job_creater = user.getUser_id();            //创建人
            BigInteger job_creater_origin = userOrigin.getOrigin_id();  //创建人所属机构
            jobConfig.setJob_creater(job_creater);
            jobConfig.setJob_creater_origin(job_creater_origin);
            fillinataskService.saveJobConfig(jobConfig);
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增填报任务失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增填报任务成功", null, "success");
    }



    //修改任务
    @RequestMapping("/updatercdjobconfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatercdjobconfig(@RequestBody JobConfig jobConfig){
        try{
            fillinataskService.updateJobConfig(jobConfig);
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改填报任务失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改填报任务成功", null, "success");
    }

    //填报任务中 填报人维护rcd_job_person_assign
    @RequestMapping("/insertrcdjobpersonassign")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcdjobpersonassign(
            @RequestParam("userid")String userid,
            @RequestParam("job_id")String job_id
    ){
        try{
          //  fillinataskService.deletercdjobpersonassign(job_id);    //把原先的删除再新增
            fillinataskService.insertrcdjobpersonassign(job_id,userid);
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增填报维护人失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增填报维护人成功", null, "success");
    }


  //查询机构对应填报人
    @RequestMapping("/huixianrcdjobpersonassign")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String huixianrcdjobpersonassign(
            @RequestParam("job_id")String job_id
    ){
        List<RcdJobPersonAssign> ll = null;
        try{
            ll =   fillinataskService.huixianrcdjobpersonassign(job_id);
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "查询机构对应填报人失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "查询机构对应填报人成功", null, ll);
    }



    //填报组维护弹框RcdJobUnitConfig  待选择列表
    @RequestMapping("/selectRcdJobUnitConfig")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectRcdJobUnitConfig(
            @RequestParam("job_id")String job_id
    ){
        List<RcdJobUnitConfig> ll = null;
        try{
            ll =  fillinataskService.selectRcdJobUnitConfig(job_id);
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报组查询未选择失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报组查询未选择成功", null, ll);
    }

    //填报组维护弹框RcdJobUnitConfig  已选择列表
    @RequestMapping("/selectRcdJobUnitConfigyi")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectRcdJobUnitConfigyi(
            @RequestParam("job_id")String job_id
    ){
        List<RcdJobUnitConfig> ll = null;
        try{
            ll =  fillinataskService.selectRcdJobUnitConfigyi(job_id);
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报组查询已选择失败", null, "error");
        }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报组查询已选择成功", null, ll);
    }


    //弹框确认
    @RequestMapping("/updateRcdJobUnitConfigyi")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updateRcdJobUnitConfigyi(
            @RequestParam("jobunitid")String jobunitid,
            @RequestParam("job_id")String job_id
    ){
        try{
              fillinataskService.updateRcdJobUnitConfigyi(jobunitid,job_id);
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报组维护弹框确认失败", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报组维护弹框确认成功", null, "success");
    }


    //填报任务审批
    @RequestMapping("/fillInTaskApprovalByJobid")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String fillInTaskApprovalByJobid(
            @RequestParam("job_id")String job_id
    ){
        try{
            fillinataskService.fillInTaskApprovalByJobid(job_id);
        }catch(Exception e){
            e.printStackTrace();
            return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "填报任务审批修改状态成功", null, "error");
        }
        return JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "填报任务审批修改状态失败", null, "success");
    }


    @RequestMapping("/checkOriginTree")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult checkOriginTree(){
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        Origin origin = originService.getOriginByUser(user.getUser_id());
        Origin originTree = originService.getAllOriginTree();
        Map<String,Origin> result = new HashMap<>();
        Integer selfLevel = this.checkSelfLevel(origin.getOrigin_id(), originTree);
        origin.setOrigin_level(selfLevel);
        result.put("selfOrigin",origin);
        result.put("originTree",originTree);
        return JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "机构树获取完成", null, result);
    }

    private Integer checkSelfLevel(BigInteger checkOriginId,Origin originTree){
        if(originTree.getOrigin_id().equals(checkOriginId)){
            return originTree.getOrigin_level();
        }else{
            Integer levelVAL = null;
            List<Origin> children = originTree.getChildren();
            if(children!=null){
                for (Origin child : children) {
                    Integer levelVALChild = this.checkSelfLevel(checkOriginId, child);
                    if(levelVALChild!=null){
                        levelVAL = levelVALChild;
                    }
                }
            }
            return levelVAL;
        }
    }

}
