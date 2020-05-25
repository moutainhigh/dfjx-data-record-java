package com.datarecord.webapp.process;

import com.datarecord.enums.ReportFileLogStatus;
import com.datarecord.webapp.process.dao.ReportFileLog;

import java.math.BigInteger;

public interface RecordFileService {
    ReportFileLog recordFileCreateLog(String reportId, String jobId, BigInteger userID, Integer logType);
    void updateFileLogStatus(Integer logId, ReportFileLogStatus logStatus, String filePath, String comment);
}
