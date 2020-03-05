package com.datarecord.webapp.dataindex.controller;

import com.datarecord.webapp.dataindex.bean.RcdDtFld;
import com.datarecord.webapp.dataindex.bean.RcdDtFldCtAssign;
import com.datarecord.webapp.dataindex.bean.RcddtCatg;
import com.datarecord.webapp.dataindex.bean.Rcddtproj;
import com.datarecord.webapp.dataindex.service.RcdDtService;
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
 * 指标Controller
 */
@Controller
@RequestMapping("/rcdDt")
public class RcdDtController {

    @Autowired
    private RcdDtService rcdDtService;


    //新增基本类型
    @RequestMapping("/insertrcddtproj")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcddtproj(
            @RequestParam("proj_name") String proj_name,
            @RequestParam("is_actived") String is_actived
    ){
        String jsonResult = "";
        if(!proj_name.isEmpty()){
            try{
                rcdDtService.insertrcddtproj(proj_name,is_actived);
            }catch(Exception e){
                e.printStackTrace();
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增基本类型失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增基本类型成功", null, "success");
    }


    //基本类型删除  一级
    @RequestMapping("/deletercddtproj")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercddtproj(
            @RequestParam("proj_id") String proj_id
    ){
        String jsonResult = "";
            try{
                List<String>  ll = new ArrayList<String>();
                ll =   rcdDtService.selectrcddtcatgproj(proj_id);
                if (ll.size() != 0){
                    return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "指标基本类别下已有关连任务无法删除", null, "error");
                }
                for(String catg_id : ll ) {
                    rcdDtService.deleteererrcddtfldI(catg_id);   // rcd_dt_fld   三级  catg_id
                }
                rcdDtService.deletercddtproj(proj_id);   //一级二级  proj_id
            }catch(Exception e){
                e.printStackTrace();
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除基本类型失败", null, "error");
            }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除基本类型成功", null, "success");
    }

    //指标类别删除  二级
    @RequestMapping("/deletrcddtcatg")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletrcddtcatg(
            @RequestParam("catg_id") String catg_id
    ){
        String jsonResult = "";
        try{
            List<String>  ll = new ArrayList<String>();
            ll =   rcdDtService.selectrcddtcatg(catg_id);
            if (ll.size() != 0){
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "指标类别下已有关连任务无法删除", null, "error");
            }else{
                rcdDtService.deletrcddtcatg(catg_id);  //rcd_dt_catg   二级   catg_id
                rcdDtService.deleteererrcddtfld(catg_id);   // rcd_dt_fld   三级  catg_id
            }
        }catch(Exception e){
            e.printStackTrace();
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除二级基本类型失败", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除二级基本类型成功", null, "success");
    }

    //指标类别删除  三级
    @RequestMapping("/deletercddtfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String deletercddtfld(
            @RequestParam("fld_id") String fld_id
    ){
        String jsonResult = "";
        try{
           int tt =  rcdDtService.selectcount(fld_id);    //查询是否有关连
            if (tt > 0){
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "此指标下已有关连任务无法删除", null, "error");
            }else{
                rcdDtService.deletercddtfld(fld_id);
            }
        }catch(Exception e){
            e.printStackTrace();
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "删除三级基本类型失败", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "删除三级基本类型成功", null, "success");
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
        String jsonResult = "";
        try{
            pageResult = rcdDtService.selectrcddtproj(currPage,pageSize);
        }catch(Exception e){
            e.printStackTrace();
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取指标体系基本类别列表失败", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取指标体系基本类别列表成功", null, pageResult);
    }


    //修改指标体系基本类别
    @RequestMapping("/updatercddtproj")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatercddtproj(
            @RequestParam("proj_id") String proj_id,
            @RequestParam("proj_name") String proj_name,
            @RequestParam("is_actived") String is_actived    ){
        String jsonResult = "";
        boolean ss = false;
        if(!proj_name.isEmpty() && !is_actived.isEmpty()){
            try{
                rcdDtService.updatercddtproj(proj_name,is_actived,proj_id);
            }catch(Exception e){
                e.printStackTrace();
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改指标体系基本类别修改失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改指标体系基本类别成功", null, "success");
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
      String jsonResult = "";
      try{
          pageResult = rcdDtService.selecttixircddtproj(currPage,pageSize,catg_id);
      }catch(Exception e){
          e.printStackTrace();
          return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取指标体系类别列表失败", null, "error");
      }
      return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取指标体系类别列表成功", null, pageResult);
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
            pageResult = rcdDtService.selecttixircddtprojer(currPage,pageSize,proj_id);
        }catch(Exception e){
            e.printStackTrace();
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取指标二级体系类别列表失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取指标二级体系类别列表成功", null, pageResult);
        return jsonResult;
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
            rcdDtService.inserttixircddtprojer(catg_name,proj_id);
        }catch(Exception e){
            e.printStackTrace();
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增二级指标体系列表失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增二级指标体系列表成功", null, "success");
        return jsonResult;
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

        String jsonResult = "";
        try{
            rcdDtService.updatetixircddtprojer(catg_id,catg_name,proj_id);
        }catch(Exception e){
            e.printStackTrace();
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改二级指标体系列表失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改二级指标体系列表成功", null, "success");
        return jsonResult;
    }


    //新增指标类型 三级
    @RequestMapping("/insertrcddtfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcddtfld(
            @RequestParam("catg_id") String catg_id,
            @RequestParam("fld_name") String fld_name,    // 名称
            @RequestParam("fld_type") String fld_type,    // 类型0:通用指标 1 突发指标
            @RequestParam("fld_data_type") String fld_data_type,
            @RequestParam("fld_visible") String fld_visible,  //可见范围：0-全部、1-移动端可见、2-PC端可见
            @RequestParam("fld_range") String fld_range,      //取值范围：0-所有、1-移动端、2-PC端
            @RequestParam("fld_is_null") String fld_is_null,
            @RequestParam("dict_content_id") String dict_content_id  //数据字典内容编码
    ){
        String jsonResult = "";
        if(!catg_id.isEmpty()  && !fld_name.isEmpty()  && !fld_data_type.isEmpty()  && !fld_is_null.isEmpty()  && !fld_type.isEmpty()){
            try{
                rcdDtService.insertrcddtfld(catg_id,fld_name,fld_data_type,fld_is_null,fld_type,fld_range,fld_visible);
              int  fid = rcdDtService.selectmax();
              String  fld_id = String.valueOf(fid);
                if(!fld_id.isEmpty() && !dict_content_id.isEmpty()){
                  /*  dict_content_id.substring(1);
                    dict_content_id.substring(0,dict_content_id.length()-1);*/
                    String[] split = dict_content_id.split(",");
                    for (String dict_contentid : split){
                        rcdDtService.insertrcddtfldctassign(fld_id,dict_contentid);
                    }
                    //指字标&数据典关系表添加
                        //rcdDtService.updatercddtdict(dict_content_id[i]);  //数据字典修改使用状态
                }
            }catch(Exception e){
                e.printStackTrace();
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增指标类型失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增指标类型成功", null, "success");
    }



    //修改指标类型
    @RequestMapping("/updatercddtfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatercddtfld(
            @RequestParam("catg_id") String catg_id,
            @RequestParam("fld_name") String fld_name,    // 名称
            @RequestParam("fld_type") String fld_type,    // 类型0:通用指标 1 突发指标
            @RequestParam("fld_data_type") String fld_data_type,
            @RequestParam("fld_is_null") String fld_is_null,
            @RequestParam("fld_visible") String fld_visible,  //可见范围：0-全部、1-移动端可见、2-PC端可见
            @RequestParam("fld_range") String fld_range,      //取值范围：0-所有、1-移动端、2-PC端
            @RequestParam("fld_id") String fld_id,    //指标id
            @RequestParam("dict_content_id") String dict_content_id  //数据字典内容编码数组
    ){
        String jsonResult = "";
        if(!catg_id.isEmpty()  && !fld_name.isEmpty()  && !fld_data_type.isEmpty()  && !fld_is_null.isEmpty()  && !fld_type.isEmpty()){
            try{
                rcdDtService.updatercddtfld(fld_id,catg_id,fld_name,fld_data_type,fld_is_null,fld_type,fld_range,fld_visible);
                if(!fld_id.isEmpty() && !dict_content_id.isEmpty() ){
                    rcdDtService.deletercddtfldctassign(fld_id);
                   /* dict_content_id.substring(1);
                    dict_content_id.substring(0,dict_content_id.length()-1);*/
                    String[] split = dict_content_id.split(",");
                    for (String dict_contentid : split){
                        rcdDtService.insertrcddtfldctassign(fld_id,dict_contentid);  //指标&数据字典关系表添加
                    }
                    //rcdDtService.updatercddtdict(dict_content_id[i]);  //数据字典修改使用状态
                }
            }catch(Exception e){
                e.printStackTrace();
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改指标类型失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改指标类型成功", null, "success");
    }


    //三级修改回显对应数据字典
    @RequestMapping("/updatehuixianrcddtfldctassign")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updatehuixianrcddtfldctassign(
            @RequestParam("fld_id") String fld_id
            ){
        List<RcdDtFldCtAssign> ll = new ArrayList<RcdDtFldCtAssign>();
        String jsonResult = "";
        try{
            ll  = rcdDtService.updatehuixianrcddtfldctassign(fld_id);
        }catch(Exception e){
            e.printStackTrace();
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改回显对应数据字典失败", null, "error");
        }
        return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改回显对应数据字典成功", null, ll);
    }




    //left基本类型
    @RequestMapping("/leftrcddtproj")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String leftrcddtproj(){
       List<Rcddtproj> ll = new ArrayList<Rcddtproj>();
        String jsonResult = "";
        try{
            ll  = rcdDtService.leftrcddtprojjblx();
        }catch(Exception e){
            e.printStackTrace();
           return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "基本类型获取失败", null, "error");
        }
        return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "基本类型获取成功", null, ll);
    }

    //left类型
    @RequestMapping("/leftrcddtcatg")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String leftrcddtcatg( @RequestParam("proj_id") String proj_id){
        List<RcddtCatg> ll = new ArrayList<RcddtCatg>();
        String jsonResult = "";
        try{
            ll  = rcdDtService.leftrcddtcatglx(proj_id);
        }catch(Exception e){
            e.printStackTrace();
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "类型获取失败", null, "error");
        }
        return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "类型获取成功", null, ll);
    }

    //left指标类型
    @RequestMapping("/leftrcddtfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String leftrcddtfld( @RequestParam("catg_id") String catg_id){
        List<RcdDtFld> ll = new ArrayList<RcdDtFld>();
        String jsonResult = "";
        try{
            ll  = rcdDtService.leftrcddtfld(catg_id);
        }catch(Exception e){
            e.printStackTrace();
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "指标类型获取失败", null, "error");
        }
        return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "指标类型获取成功", null, ll);
    }










}
