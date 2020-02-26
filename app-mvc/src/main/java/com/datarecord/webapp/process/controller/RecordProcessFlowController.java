package com.datarecord.webapp.process.controller;

import com.datarecord.webapp.process.service.RecordProcessFactory;
import com.datarecord.webapp.process.service.RecordProcessFlowService;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("record/flow")
public class RecordProcessFlowController {

    @Autowired
    private RecordProcessFlowService recordProcessFlowService;

    @RequestMapping("pageJob")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult pageJob(@RequestParam("currPage") String currPage, @RequestParam("pageSize") String pageSize){
        PageResult pageData = recordProcessFlowService.pageJob(currPage, pageSize);

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "获取成功", null, pageData);
        return successResult;
    }

}
