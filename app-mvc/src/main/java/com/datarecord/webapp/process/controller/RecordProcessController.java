package com.datarecord.webapp.process.controller;


import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("record/process")
public class RecordProcessController {

    @RequestMapping("makeJob")
    public JsonResult makeJob(String jobId){

        JsonResult successResult = JsonSupport.makeJsonpResult(
                JsonResult.RESULT.SUCCESS, "发布流程已驱动", null, JsonResult.RESULT.SUCCESS.toString());
        return successResult;
    }

}
