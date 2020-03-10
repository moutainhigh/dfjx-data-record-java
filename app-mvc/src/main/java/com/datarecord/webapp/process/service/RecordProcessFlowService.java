package com.datarecord.webapp.process.service;

import com.webapp.support.page.PageResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

public interface RecordProcessFlowService {

    PageResult pageJob(@RequestParam("currPage") String currPage, @RequestParam("pageSize") String pageSize, String reportStatus, String reportName, String reportOrigin);

    void reviewFld(String fldId,String reviewStatus);

    void reviewUnit(String unitId, String reviewStatus);

    void reviewJobItems(String jobId, String reviewStatus);


    PageResult pageReviewJobs(int user_id, String currPage, String pageSize, Map<String,String> queryParams);

    PageResult pageReviewFlds(int user_id, String currPage, String pageSize,Map<String,String> queryParams);

}
