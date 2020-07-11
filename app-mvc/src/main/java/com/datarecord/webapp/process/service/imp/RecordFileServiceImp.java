package com.datarecord.webapp.process.service.imp;

import com.datarecord.enums.ReportFileLogStatus;
import com.datarecord.webapp.process.RecordFileService;
import com.datarecord.webapp.process.dao.IRecordFileDao;
import com.datarecord.webapp.process.dao.IReportSumDao;
import com.datarecord.webapp.process.dao.ReportFileLog;
import com.google.common.base.Strings;
import com.webapp.support.utils.DateSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;

@Service("recordFileService")
public class RecordFileServiceImp implements RecordFileService {

    @Autowired
    private IReportSumDao reportSumDao;

    @Autowired
    private IRecordFileDao recordFileLog;

    @Override
    public ReportFileLog recordFileCreateLog(String reportId, String jobId, BigInteger userID, Integer logType) {
        ReportFileLog reportFileLog = new ReportFileLog();
        reportFileLog.setReport_id(Strings.isNullOrEmpty(reportId) ? -820 : new Integer(reportId));
        reportFileLog.setJob_id(new Integer(jobId));
        reportFileLog.setLog_status(ReportFileLogStatus.CREATING.getValue());
        reportFileLog.setStart_time(DateSupport.getBeijingTime());
        reportFileLog.setLog_user(userID);
        reportFileLog.setLog_type(logType);
        recordFileLog.recordFileLog(reportFileLog);
        return reportFileLog;
    }
    @Override
    public void updateFileLogStatus(Integer logId,ReportFileLogStatus logStatus,String filePath,String comment){
        ReportFileLog reportFileLog = new ReportFileLog();
        reportFileLog.setLog_id(logId);
        reportFileLog.setFile_path(filePath);
        reportFileLog.setComment(comment);
        reportFileLog.setLog_status(logStatus.getValue());
        reportFileLog.setEnd_time(DateSupport.getBeijingTime());
        recordFileLog.updateRecordFileLog(reportFileLog);
    }
}
