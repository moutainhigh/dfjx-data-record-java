package dataindex.controller;

import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import dataindex.bean.RcdDtFld;
import dataindex.bean.RcddtCatg;
import dataindex.bean.Rcddtproj;
import dataindex.service.RcdDtService;
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
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增数据字典类型失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增数据字典类型成功", null, "success");
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
            jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取指标体系基本类别列表失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取指标体系基本类别列表成功", null, pageResult);
        return jsonResult;
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
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "修改指标体系基本类别修改失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "修改指标体系基本类别成功", null, "success");
    }








  //指标体系类别列表
  @RequestMapping("/selecttixircddtproj")
  @ResponseBody
  @CrossOrigin(allowCredentials="true")
  public String selecttixircddtproj(
          @RequestParam("currPage") int currPage,
          @RequestParam("pageSize")int pageSize,
          @RequestParam("catg_id")String catg_id     //rcd_dt_catg 指标类别编码
  ){
      PageResult pageResult = null;
      String jsonResult = "";
      try{
          pageResult = rcdDtService.selecttixircddtproj(currPage,pageSize,catg_id);
      }catch(Exception e){
          jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取指标体系类别列表失败", null, "error");
      }
      jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取指标体系类别列表成功", null, pageResult);
      return jsonResult;
  }

    //新增指标类型
    @RequestMapping("/insertrcddtfld")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertrcddtfld(
            @RequestParam("catg_id") String catg_id,
            @RequestParam("fld_name") String fld_name,    // 名称
            @RequestParam("fld_point") String fld_point,  //单位
            @RequestParam("fld_type") String fld_type,  //类型
            @RequestParam("fld_data_type") String fld_data_type,
            @RequestParam("fld_is_null") String fld_is_null,
            @RequestParam("fld_id") String fld_id,    //指标id
            @RequestParam("dict_id") String[] dict_content_id  //数据字典内容编码
    ){
        String jsonResult = "";
        if(!catg_id.isEmpty()  && !fld_name.isEmpty()  && !fld_data_type.isEmpty()  && !fld_is_null.isEmpty()  && !fld_point.isEmpty() && !fld_type.isEmpty()){
            try{
                rcdDtService.insertrcddtfld(catg_id,fld_name,fld_point,fld_type,fld_data_type,fld_is_null);
                if(!fld_id.isEmpty() && dict_content_id.length>0 ){
                    for (int  i= 0; i<dict_content_id.length; i++){
                        rcdDtService.insertrcddtfldctassign(fld_id,dict_content_id[i]);  //指标&数据字典关系表添加
                        //rcdDtService.updatercddtdict(dict_content_id[i]);  //数据字典修改使用状态
                    }
                }
            }catch(Exception e){
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
            @RequestParam("fld_point") String fld_point,  //单位
            @RequestParam("fld_type") String fld_type,  //类型
            @RequestParam("fld_data_type") String fld_data_type,
            @RequestParam("fld_is_null") String fld_is_null,
            @RequestParam("fld_id") String fld_id,    //指标id
            @RequestParam("dict_id") String[] dict_content_id  //数据字典内容编码
    ){
        String jsonResult = "";
        if(!catg_id.isEmpty()  && !fld_name.isEmpty()  && !fld_data_type.isEmpty()  && !fld_is_null.isEmpty()  && !fld_point.isEmpty() && !fld_type.isEmpty()){
            try{
                rcdDtService.updatercddtfld(fld_id,catg_id,fld_name,fld_point,fld_type,fld_data_type,fld_is_null);
                if(!fld_id.isEmpty() && dict_content_id.length>0 ){
                    rcdDtService.deletercddtfldctassign(fld_id);
                    for (int  i= 0; i<dict_content_id.length; i++){
                        rcdDtService.insertrcddtfldctassign(fld_id,dict_content_id[i]);  //指标&数据字典关系表添加
                        //rcdDtService.updatercddtdict(dict_content_id[i]);  //数据字典修改使用状态
                    }
                }
            }catch(Exception e){
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增指标类型失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增指标类型成功", null, "success");
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
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "指标类型获取失败", null, "error");
        }
        return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "指标类型获取成功", null, ll);
    }










}
