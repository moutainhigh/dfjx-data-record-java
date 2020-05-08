package com.datarecord.webapp.process.service;

import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobUnitConfig;
import com.webapp.support.jsonp.JsonResult;

import java.util.Map;

public interface RecordMaker {

    Map<JsonResult.RESULT,Object> makeJob(String jobId);
    Map<JsonResult.RESULT,Object> preMake(String jobId);
    void createRecordDatas(JobUnitConfig jobUnit, Integer reportId);
    void createSimpleRecordData(JobUnitConfig jobUnit,Integer reportId,Integer columId);
    void createGridRecordDatas(JobUnitConfig jobUnit,Integer reportId,Integer columId);
}
