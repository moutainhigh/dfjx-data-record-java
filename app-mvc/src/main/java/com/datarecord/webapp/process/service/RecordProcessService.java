package com.datarecord.webapp.process.service;

import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.process.entity.*;
import com.webapp.support.page.PageResult;

import java.util.List;
import java.util.Map;

public interface RecordProcessService {

    void makeJob(String jobId);

    PageResult pageJob(int user_id, String currPage, String pageSize);

    JobConfig getJobConfigByReportId(String reportId);

    List<ReportFldTypeConfig> getFldByUnitId(String unitId);

    List<ReportJobData> getFldReportDatas(String jobId,String reportId, String groupId);

    Map<Integer, List<DataDictionary>> getUnitDictFldContent(String groupId);

    void saveGridDatas(SaveReportJobInfos reportJobInfo);

    Map<Integer, Map<Integer, String>> validateGridDatas(List<ReportJobData> reportJobDataList, String unitId);

    List<ReportFldTypeConfig> getClientFldByUnitId(String groupId, String clientType);
}
