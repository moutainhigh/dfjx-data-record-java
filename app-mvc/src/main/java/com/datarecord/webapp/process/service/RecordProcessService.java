package com.datarecord.webapp.process.service;

import com.datarecord.enums.ReportFldStatus;
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

    List<ReportJobInfo> getReportJobInfosByJobId(String jobId);

    List<ReportJobInfo> checkReportStatus(List<ReportJobInfo> dataList);

    List<DataDictionary> getDictcontent4Fld(Integer fld_id);

    List<ReportJobData> getUnitDatas(String jobId,String reportId,String unitId);

    List<Integer> getUnitColums(String jobId,String reportId,String unitId);

    List<Map<Integer, Map<Integer, String>>> validatePcReport(String reportId);

    void doCommitAuth(String reportId, ReportFldStatus submit);

    Integer getMaxColumId(String jobId,String reportId);

    void closeReportByJobId(String jobId);
}
