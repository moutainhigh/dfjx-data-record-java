package com.datarecord.webapp.datadictionary.controller;
import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.datadictionary.service.DataDictionaryService;
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
 * 数据字典Controller
 */
@Controller
@RequestMapping("/dictionary")
public class DataDictionaryController {

    @Autowired
    private DataDictionaryService dataDictionaryService;


    //新增数据字典类型
    @RequestMapping("/insertDataDictionary")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String insertDataDictionary(
            @RequestParam("dict_name") String dict_name){
        String jsonResult = "";
        if(!dict_name.isEmpty()){
            try{
                dataDictionaryService.insertDataDictionary(dict_name);
            }catch(Exception e){
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增数据字典类型失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增数据字典类型成功", null, "success");
    }


    //修改数据类型
    @RequestMapping("/updateDataDictionarybydictid")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updateDataDictionarybydictid(
            @RequestParam("dict_id") String dict_id,
            @RequestParam("dict_name")String dict_name
    ){
        String jsonResult = "";
            try{
                dataDictionaryService.updateDataDictionarybydictid(dict_id,dict_name);
            }catch(Exception e){
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "数据字典修改失败", null, "error");
            }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "数据字典修改成功", null, "success");
    }

    //数据字典List
    @RequestMapping("/dataDictionarylist")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String dataDictionarylist(
            @RequestParam("currPage") int currPage,
            @RequestParam("pageSize")int pageSize
    ){
        PageResult pageResult = null;
        String jsonResult = "";
        try{
            pageResult = dataDictionaryService.dataDictionarylist(currPage,pageSize);
        }catch(Exception e){
            jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取数据字典列表失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取数据字典列表成功", null, pageResult);
        return jsonResult;
    }



    //按照类型查看数据字段内容
    @RequestMapping("/selectDataDictionary")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectDataDictionary(
            @RequestParam("currPage") int currPage,
            @RequestParam("pageSize")int pageSize,
            @RequestParam("dict_id")String dict_id
            ){
        PageResult pageResult = null;
        String jsonResult = "";
        try{
            pageResult = dataDictionaryService.selectDataDictionary(currPage,pageSize,dict_id);
        }catch(Exception e){
            return jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取数据内容列表失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取数据内容列表成功", null, pageResult);
        return jsonResult;
    }


     //按照类型新增数据字段内容
     @RequestMapping("/inserttypeDataDictionary")
     @ResponseBody
     @CrossOrigin(allowCredentials="true")
     public String inserttypeDataDictionary(
             @RequestParam("dict_id") String dict_id,
             @RequestParam("dict_content_name") String dict_content_name,
             @RequestParam("dict_content_value") String dict_content_value
             ){
         String jsonResult = "";
         if(!dict_content_name.isEmpty() && !dict_content_value.isEmpty() && !dict_id.isEmpty()){
             try{
                 dataDictionaryService.inserttypeDataDictionary(dict_id,dict_content_name,dict_content_value);
             }catch(Exception e){
                 return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "新增数据字典内容失败", null, "error");
             }
         }else{
             return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
         }
         return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "新增数据字典内容成功", null, "success");
     }



    //修改数据字段内容
    @RequestMapping("/updateDataDictionary")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String updateDataDictionary(
            @RequestParam("dict_content_id") String dict_content_id,
            @RequestParam("dict_id") String dict_id,
            @RequestParam("dict_content_name") String dict_content_name,
            @RequestParam("dict_content_value") String dict_content_value
    ){
        String jsonResult = "";
        boolean ss = false;
        if(!dict_content_id.isEmpty() && !dict_id.isEmpty() && !dict_content_name.isEmpty()  && !dict_content_value.isEmpty()){
            try{
                dataDictionaryService.updateDataDictionary(dict_content_id,dict_id,dict_content_name,dict_content_value);
            }catch(Exception e){
                return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "数据字段内容修改失败", null, "error");
            }
        }else{
            return   jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "请确认必填项是否填写内容", null, "error");
        }
        return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "数据字段内容修改成功", null, "success");
    }

    //左侧菜单数据
    @RequestMapping("/selectleftDataDictionary")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String selectleftDataDictionary(){
      List<DataDictionary> ll= new ArrayList<DataDictionary>();
        String jsonResult = "";
        try{
            ll = dataDictionaryService.selectleftDataDictionary();
        }catch(Exception e){
            return  jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD, "获取菜单列表失败", null, "error");
        }
        jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取菜单列表成功", null, ll);
        return jsonResult;
    }


}
