package com.datarecord.webapp.process.service;

import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.enums.ReportStatus;
import com.datarecord.webapp.process.entity.*;
import com.webapp.support.page.PageResult;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface RecordProcessService {

    PageResult pageJob(BigInteger user_id, String currPage, String pageSize, Map<String,String> queryParams);

    JobConfig getJobConfigByReportId(String reportId);

    JobConfig getJobConfigByJobId(String jobId);

    List<ReportFldTypeConfig> getFldByUnitId(String unitId);

    List<ReportJobData> getFldReportDatas(String jobId,String reportId, String groupId);

    Map<Integer, List<DataDictionary>> getUnitDictFldContent(String groupId);

    void saveDatas(SaveReportJobInfos reportJobInfo);

    Map<Integer, Map<Integer, String>> validateDatas(List<ReportJobData> reportJobDataList, String unitId,String clientType);

    List<ReportFldTypeConfig> getClientFldByUnitId(String groupId, String clientType);

    void updateReportStatus(String reportId, ReportStatus reportDone);

    ReportJobInfo getReportJobInfo(String reportId);
}
