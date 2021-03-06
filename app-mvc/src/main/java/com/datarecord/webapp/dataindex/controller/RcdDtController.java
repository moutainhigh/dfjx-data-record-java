package com.datarecord.webapp.dataindex.controller;

import com.datarecord.enums.FldConfigStatus;
import com.datarecord.webapp.dataindex.bean.RcdDtFld;
import com.datarecord.webapp.dataindex.bean.RcdDtFldCtAssign;
import com.datarecord.webapp.dataindex.bean.RcddtCatg;
import com.datarecord.webapp.dataindex.bean.Rcddtproj;
import com.datarecord.webapp.dataindex.service.RcdDtService;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.datarecord.webapp.reportinggroup.bean.ReportingGroup;
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
import java.util.ArrayList;
import java.util.List;


/**
 * 指标Controller
 */
@Controller
@RequestMapping("/rcdDt")
public class RcdDtController {

    @Autowired
    private RcdDtService rcdDtService;

    @Autowired
    private OriginService originService;



    //新增基本类型
    @RequestMapping("/insertrcddtproj")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcddtproj(
            @RequestParam("proj_name") String proj_name,
            @RequestParam("is_actived") String is_actived
    ){
        if(!proj_name.isEmpty()){
            try{
                User user = WorkbenchShiroUtils.checkUserFromShiroContext();
                Origin userOrigin = originService.getOriginByUser(user.getUser_id());
                if(userOrigin!=null){
                    BigInteger user_id =   user.getUser_id(); //创建人
                    BigInteger originid =    userOrigin.getOrigin_id(); //创建人所属机构
                    rcdDtService.insertrcddtproj(proj_name,is_actived,String.valueOf(user_id),String.valueOf(originid));
                }else{
                    return JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "当前用户无关联结构", "当前用户无关联结构", "error");
                }

            }catch(Exception e){
                e.printStackTrace();
                return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增基本类型失败", null, "error");
            }
        }else{
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增基本类型成功", null, "success");
    }


    //基本类型删除  一级
    @RequestMapping("/deletercddtproj")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercddtproj(
            @RequestParam("proj_id") String proj_id
    ){
            try{
                List<String>  ll = null;
                ll =   rcdDtService.selectrcddtcatgproj(proj_id);
                if (ll.size() != 0){
                    return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "指标基本类别下已有关连任务无法删除", null, "error");
                }
                for(String catg_id : ll ) {
                    rcdDtService.deleteererrcddtfldI(catg_id);   // rcd_dt_fld   三级  catg_id
                }
                rcdDtService.deletercddtproj(proj_id);   //一级二级  proj_id
            }catch(Exception e){
                e.printStackTrace();
                return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除基本类型失败", null, "error");
            }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除基本类型成功", null, "success");
    }

    //指标类别删除  二级
    @RequestMapping("/deletrcddtcatg")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletrcddtcatg(
            @RequestParam("catg_id") String catg_id
    ){
        try{
            List<String>  ll = null;
            ll =   rcdDtService.selectrcddtcatg(catg_id);
            if (ll.size() != 0){
                return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "指标类别下已有关连任务无法删除", null, "error");
            }else{
                rcdDtService.deletrcddtcatg(catg_id);  //rcd_dt_catg   二级   catg_id
            }
        }catch(Exception e){
            e.printStackTrace();
            return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除二级基本类型失败", null, "error");
        }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除二级基本类型成功", null, "success");
    }

    //指标类别删除  三级
    @RequestMapping("/deletercddtfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercddtfld(
            @RequestParam("fld_id") String fld_id
    ){
        try{
           int tt =  rcdDtService.selectcount(fld_id);    //查询是否有关连
            if (tt > 0){
                return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "此指标下已有关连任务无法删除", null, "error");
            }else{
                rcdDtService.deletercddtfld(fld_id);
            }
        }catch(Exception e){
            e.printStackTrace();
            return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除三级基本类型失败", null, "error");
        }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除三级基本类型成功", null, "success");
    }


    //指标体系基本类别列表
    @RequestMapping("/selectrcddtproj")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectrcddtproj(
            @RequestParam("currPage") int currPage,
            @RequestParam("pageSize")int pageSize
    ){
        PageResult pageResult = null;
            try{
            User user = WorkbenchShiroUtils.checkUserFromShiroContext();
            Origin userOrigin = originService.getOriginByUser(user.getUser_id());
            if(UserType.SYSMANAGER.compareTo(user.getUser_type())){
                pageResult = rcdDtService.selectrcddtproj(currPage,pageSize,userOrigin,null);
            }else{
                pageResult = rcdDtService.selectrcddtproj(currPage,pageSize,userOrigin,String.valueOf(user.getUser_id()));
            }
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取指标体系基本类别列表失败", null, "error");
        }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取指标体系基本类别列表成功", null, pageResult);
    }


    //修改指标体系基本类别
    @RequestMapping("/updatercddtproj")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatercddtproj(
            @RequestParam("proj_id") String proj_id,
            @RequestParam("proj_name") String proj_name,
            @RequestParam("is_actived") String is_actived    ){
        if(!proj_name.isEmpty() && !is_actived.isEmpty()){
            try{
                rcdDtService.updatercddtproj(proj_name,is_actived,proj_id);
            }catch(Exception e){
                e.printStackTrace();
                return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改指标体系基本类别修改失败", null, "error");
            }
        }else{
            return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改指标体系基本类别成功", null, "success");
    }






  //根据指标类别查询指标体系类别列表  三级
  @RequestMapping("/selecttixircddtproj")
  @ResponseBody
  @CrossOrigin(allowCredentials="true")
  public String selecttixircddtproj(
          @RequestParam("currPage") int currPage,
          @RequestParam("pageSize")int pageSize,
          @RequestParam("catg_id")String catg_id     // 指标类别编码
  ){
      PageResult pageResult = null;

      try{
          User user = WorkbenchShiroUtils.checkUserFromShiroContext();
          if(UserType.SYSMANAGER.compareTo(user.getUser_type())){
              pageResult = rcdDtService.selecttixircddtproj(currPage,pageSize,catg_id,null);
          }else{
              pageResult = rcdDtService.selecttixircddtproj(currPage,pageSize,catg_id,String.valueOf(user.getUser_id()));
          }
      }catch(Exception e){
          e.printStackTrace();
          return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取指标体系类别列表失败", null, "error");
      }
      return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取指标体系类别列表成功", null, pageResult);
  }

    //根据指标类别查询指标体系类别列表  二级
    @RequestMapping("/selecttixircddtprojer")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selecttixircddtprojer(
            @RequestParam("currPage") int currPage,
            @RequestParam("pageSize")int pageSize,
            @RequestParam("proj_id")String proj_id     // 指标类别编码
    ){
        PageResult pageResult = null;
        String jsonResult = "";
        try{
            User user = WorkbenchShiroUtils.checkUserFromShiroContext();
            Origin userOrigin = originService.getOriginByUser(user.getUser_id());
            if(UserType.SYSMANAGER.compareTo(user.getUser_type())){
                pageResult = rcdDtService.selecttixircddtprojer(currPage,pageSize,proj_id,userOrigin,null);
            }else{
                pageResult = rcdDtService.selecttixircddtprojer(currPage,pageSize,proj_id,userOrigin,String.valueOf(user.getUser_id()));
            }
        }catch(Exception e){
            e.printStackTrace();
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取指标二级体系类别列表失败", null, "error");
        }
      return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取指标二级体系类别列表成功", null, pageResult);

    }


    //新增指标体系类别列表  二级
    @RequestMapping("/inserttixircddtprojer")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String inserttixircddtprojer(
            @RequestParam("catg_name") String catg_name,
            @RequestParam("proj_id")String proj_id     // 指标类别编码
    ){
        String jsonResult = "";
        try{
            User user = WorkbenchShiroUtils.checkUserFromShiroContext();
            Origin userOrigin = originService.getOriginByUser(user.getUser_id());
             BigInteger user_id =   user.getUser_id(); //创建人
            BigInteger originid =    userOrigin.getOrigin_id(); //创建人所属机构
            rcdDtService.inserttixircddtprojer(catg_name,proj_id,String.valueOf(user_id),String.valueOf(originid));
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增二级指标体系列表失败", null, "error");
        }
      return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增二级指标体系列表成功", null, "success");

    }


    //修改指标体系类别列表  二级
    @RequestMapping("/updatetixircddtprojer")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatetixircddtprojer(
            @RequestParam("catg_id") String catg_id,
            @RequestParam("catg_name") String catg_name,
            @RequestParam("proj_id")String proj_id     // 指标类别编码
    ){
        try{
            rcdDtService.updatetixircddtprojer(catg_id,catg_name,proj_id);
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改二级指标体系列表失败", null, "error");
        }
     return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改二级指标体系列表成功", null, "success");

    }


    //新增指标类型 三级
    @RequestMapping("/insertrcddtfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcddtfld(
            @RequestBody ReportFldConfig reportFldConfig
    ){
            try{
                User user = WorkbenchShiroUtils.checkUserFromShiroContext();
                Origin userOrigin = originService.getOriginByUser(user.getUser_id());
                reportFldConfig.setFld_creater(user.getUser_id()); //创建人
                reportFldConfig.setFld_creater_origin(userOrigin.getOrigin_id()); //创建人所属机构
                reportFldConfig.setFld_status(new Integer(FldConfigStatus.APPROVE.getValue()));
                rcdDtService.insertrcddtfld(reportFldConfig);
              int  fid = rcdDtService.selectmax();
              String  fld_id = String.valueOf(fid);
                if(!fld_id.isEmpty() && !reportFldConfig.getDict_content_id().isEmpty()){
                    String[] split = reportFldConfig.getDict_content_id().split(",");
                    for (String dict_contentid : split){
                        rcdDtService.insertrcddtfldctassign(fld_id,dict_contentid);
                    }
                    //指字标&数据典关系表添加
                        //rcdDtService.updatercddtdict(dict_content_id[i]);  //数据字典修改使用状态
                }
            }catch(Exception e){
                e.printStackTrace();
                return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增指标类型失败", null, "error");
            }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增指标类型成功", null, "success");
    }



    //修改指标类型
    @RequestMapping("/updatercddtfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatercddtfld(
           @RequestBody ReportFldConfig reportFldConfig
    ){
            try{
                rcdDtService.updatercddtfld(reportFldConfig);
                if(reportFldConfig.getFld_id() != null  && !reportFldConfig.getDict_content_id().isEmpty() ){
                    String fld_id = reportFldConfig.getFld_id().toString();
                    rcdDtService.deletercddtfldctassign(fld_id);
                   /* dict_content_id.substring(1);
                    dict_content_id.substring(0,dict_content_id.length()-1);*/
                    String[] split = reportFldConfig.getDict_content_id().split(",");
                    for (String dict_contentid : split){
                        rcdDtService.insertrcddtfldctassign(fld_id,dict_contentid);  //指标&数据字典关系表添加
                    }
                    //rcdDtService.updatercddtdict(dict_content_id[i]);  //数据字典修改使用状态
                }
            }catch(Exception e){
                e.printStackTrace();
                return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改指标类型失败", null, "error");
            }
        return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改指标类型成功", null, "success");
    }


    //三级修改回显对应数据字典
    @RequestMapping("/updatehuixianrcddtfldctassign")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatehuixianrcddtfldctassign(
            @RequestParam("fld_id") String fld_id
            ){
        List<RcdDtFldCtAssign> ll = null;
        String jsonResult = "";
        try{
            ll  = rcdDtService.updatehuixianrcddtfldctassign(fld_id);
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改回显对应数据字典失败", null, "error");
        }
        return     JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改回显对应数据字典成功", null, ll);
    }




    //left基本类型
    @RequestMapping("/leftrcddtproj")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String leftrcddtproj(){
       List<Rcddtproj> ll = null;
        try{
            User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        //    Origin userOrigin = originService.getOriginByUser(user.getUser_id());
            if(UserType.SYSMANAGER.compareTo(user.getUser_type())){
                ll  = rcdDtService.leftrcddtprojjblx(null);
            }else{
                ll  = rcdDtService.leftrcddtprojjblx(String.valueOf(user.getUser_id()));
            }
        }catch(Exception e){
            e.printStackTrace();
           return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "基本类型获取失败", null, "error");
        }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "基本类型获取成功", null, ll);
    }

    //left类型
    @RequestMapping("/leftrcddtcatg")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String leftrcddtcatg( @RequestParam("proj_id") String proj_id){
        List<RcddtCatg> ll =null;
        try{
            User user = WorkbenchShiroUtils.checkUserFromShiroContext();
            Origin userOrigin = originService.getOriginByUser(user.getUser_id());
            if(UserType.SYSMANAGER.compareTo(user.getUser_type())){
                ll  = rcdDtService.leftrcddtcatglx(proj_id,null);
            }else{
                ll  = rcdDtService.leftrcddtcatglx(proj_id,String.valueOf(user.getUser_id()));
            }
           // Origin userOrigin = originService.getOriginByUser(user.getUser_id());
        }catch(Exception e){
            e.printStackTrace();
            return   JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "类型获取失败", null, "error");
        }
        return  JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "类型获取成功", null, ll);
    }

    //left指标类型
    @RequestMapping("/leftrcddtfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String leftrcddtfld( @RequestParam("catg_id") String catg_id){
        List<RcdDtFld> ll = null;
        try{
            User user = WorkbenchShiroUtils.checkUserFromShiroContext();
            if(UserType.SYSMANAGER.compareTo(user.getUser_type())){
                ll  = rcdDtService.leftrcddtfld(catg_id,null);
            }else{
                ll  = rcdDtService.leftrcddtfld(catg_id,String.valueOf(user.getUser_id()));
            }
        }catch(Exception e){
            e.printStackTrace();
            return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "指标类型获取失败", null, "error");
        }
        return    JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "指标类型获取成功", null, ll);
    }










}
