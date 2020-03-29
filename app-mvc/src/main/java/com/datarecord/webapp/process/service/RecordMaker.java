package com.datarecord.webapp.process.service;

import com.datarecord.webapp.process.entity.JobConfig;
import com.webapp.support.jsonp.JsonResult;

import java.util.Map;

public interface RecordMaker {

    Map<JsonResult.RESULT,Object> makeJob(String jobId);
    Map<JsonResult.RESULT,Object> preMake(JobConfig jobConfigEntity);

}