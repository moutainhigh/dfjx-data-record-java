package com.datarecord.webapp.sys.origin.controller;

import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.spring.aop.annotation.JsonpCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("origin")
public class OriginController {

    @Autowired
    private OriginService originService;

    @RequestMapping("listOrigin")
    @ResponseBody
    @JsonpCallback
    @CrossOrigin(allowCredentials="true")
    public String listOrigin(int currPage, int pageSize){
        PageResult originList = originService.listOrigin(currPage, pageSize);

        String jsonpResponse = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取成功", null, originList);

        return jsonpResponse;
    }

    @RequestMapping("listAllOrigin")
    @ResponseBody
    @JsonpCallback
    @CrossOrigin(allowCredentials="true")
    public String listAllOrigin(){
        List<Origin> originList = originService.listAllOrigin();

        String jsonpResponse = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取成功", null, originList);

        return jsonpResponse;
    }

    @RequestMapping("userOriginSave")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult userOriginSave(Integer originId, Integer userId){
        originService.userOriginSave(originId,userId);
        JsonResult jsonpResponse = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null, null);
        return jsonpResponse;
    }

    @RequestMapping("getOriginByUser")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getOriginByUser(Integer userId){
        Origin origin = originService.getOriginByUser(userId);
        JsonResult jsonpResponse = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null, origin);
        return jsonpResponse;
    }

    @RequestMapping("getAllOriginTree")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult getAllOriginTree(){
        Origin origin = originService.getAllOriginTree();
        JsonResult jsonpResponse = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "保存成功", null, origin);
        return jsonpResponse;
    }
}
