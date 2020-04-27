package com.datarecord.webapp.process.service;

import com.datarecord.webapp.process.dao.ReportFileLog;
import com.datarecord.webapp.process.entity.ExportParams;
import com.datarecord.webapp.process.entity.ReportJobData;

import java.util.List;
import java.util.Map;

public interface ReportSumService {
    Map<Integer, List<ReportJobData>> recordDataByFlds(ExportParams exportParams);

    void exportRecordFldsData(ExportParams exportParams);

    String exportGroup(String reportId,String jobId, String groupId);

    ReportFileLog getReportFile(String logId);
}
