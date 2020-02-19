package com.datarecord.webapp.process.service.imp;

import com.datarecord.webapp.process.entity.ReportJobData;
import com.datarecord.webapp.process.entity.SaveReportJobInfos;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("simpleRecordProcessService")
public class SimpleRecordProcessService extends AbstractRecordProcessServiceImp {

    @Override
    public void saveDatas(SaveReportJobInfos reportJobInfo) {
        List<ReportJobData> reportJobInfos = reportJobInfo.getReportJobInfos();
        if(reportJobInfos!=null&&reportJobInfos.size()>0){
            for (ReportJobData jobInfo : reportJobInfos) {
                recordProcessDao.updateReportJobData(jobInfo,jobInfo.getColum_id(),reportJobInfo.getJob_id());
            }
        }
    }

    @Override
    public Map<Integer, Map<Integer, String>> validateDatas(List<ReportJobData> reportJobDataList, String unitId){

        Map<Integer, Map<Integer, String>> validateResultMap = super.validateDatas(reportJobDataList, unitId);

        return validateResultMap;
    }

}
