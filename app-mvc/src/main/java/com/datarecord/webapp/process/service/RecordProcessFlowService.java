package com.datarecord.webapp.process.service;

import com.datarecord.webapp.process.dao.ReportFileLog;
import com.datarecord.webapp.process.entity.ExportParams;
import com.datarecord.webapp.process.entity.JobFlowLog;
import com.webapp.support.page.PageResult;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface RecordProcessFlowService {

    PageResult pageJob(@RequestParam("currPage") String currPage, @RequestParam("pageSize") String pageSize, String reportStatus, String reportName, String reportOrigin);

    void reviewFld(String fldId,String reviewStatus);

    void reviewUnit(String unitId, String reviewStatus);

    void reviewJobItems( JobFlowLog jobFlowLog);


    PageResult pageReviewJobConfigs(String user_id, String currPage, String pageSize, Map<String,String> queryParams);

    PageResult pageReviewFlds(String user_id, String currPage, String pageSize, Map<String,String> queryParams);

    void subJobConfig(String jobId);

    List<JobFlowLog> getJobFlowLogs(String jobId);

    PageResult pageReportDatas(BigInteger user_id, String currPage, String pageSize, Map<String, String> queryParams);

    PageResult pageReviewDatas(String currPage, String pageSize, String jobId,String reportStatus);

    List<ReportFileLog> listReportFile(String reportId);
}
