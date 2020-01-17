package com.datarecord.webapp.process.service.imp;

import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.dataindex.bean.FldDataTypes;
import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("recordProcessService")
public class RecordProcessServiceImp implements RecordProcessService {

    private Logger logger = LoggerFactory.getLogger(RecordProcessServiceImp.class);

    private boolean debugger = true;

    @Autowired
    private IRecordProcessDao recordProcessDao;

    @Override
    public void makeJob(String jobId) {
        JobConfig jobConfigEntity = recordProcessDao.getJobConfigByJobId(jobId);
        logger.info("job config entity : {}",jobConfigEntity);
        //根据任务建表,每个填报任务对应一张表
        recordProcessDao.dropJobDataTable(jobId);
        recordProcessDao.makeJobDataTable(jobId);

        //根据任务对应的填报人和填报单位,下发任务

        String debuggerMainThreadWait = "";

        new Thread(new Runnable() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public void run() {
                logger.info("开始发布填报任务:{}",jobConfigEntity.getJob_name());
                recordProcessDao.changeRecordJobStatus(jobId, JobConfigStatus.SUBMITING.value() );

                List<JobUnitConfig> jobUnits = jobConfigEntity.getJobUnits();
                for (JobUnitConfig jobUnit : jobUnits) {
                    List<ReportFldConfig> unitFlds = jobUnit.getUnitFlds();
                    List<JobPerson> allJobPerson = jobConfigEntity.getJobPersons();

                    for (JobPerson jobPerson : allJobPerson) {
                        Integer originId = jobPerson.getOrigin_id();
                        Integer userId = jobPerson.getUser_id();
                        ReportJobInfo reportJobInfo = new ReportJobInfo();
                        reportJobInfo.setJob_id(new Integer(jobId));
                        reportJobInfo.setRecord_origin_id(originId);
                        reportJobInfo.setRecord_status(JobConfigStatus.NORMAL.value());
                        reportJobInfo.setRecord_user_id(userId);

                        recordProcessDao.createReportJobInfo(reportJobInfo);
                        Integer reportId = reportJobInfo.getReport_id();

                        for (ReportFldConfig unitFld : unitFlds) {
                            int fldId = unitFld.getFld_id();
                            ReportJobData reportJobData = new ReportJobData();
                            reportJobData.setColum_id(0);
                            reportJobData.setFld_id(fldId);
                            reportJobData.setReport_id(reportId);
                            reportJobData.setUnit_id(jobUnit.getJob_unit_id());
                            reportJobData.setRecord_data("预设值");
                            recordProcessDao.createRcdReortJobData(reportJobData,jobId);
                        }
                    }

                }

                logger.info("填报任务发布完成:{}",jobConfigEntity.getJob_name());
                recordProcessDao.changeRecordJobStatus(jobId, JobConfigStatus.SUBMIT.value());
                if(debugger){
                    synchronized (debuggerMainThreadWait){
                        debuggerMainThreadWait.notify();
                    }
                }
            }
        },new StringBuilder().append("填报任务发布").append("-").append(jobId).append("-").append(jobConfigEntity.getJob_name()).toString()).start();

        if(debugger){
            synchronized (debuggerMainThreadWait){
                try {
                    debuggerMainThreadWait.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public PageResult pageJob(int user_id, String currPage, String pageSize) {
        if(Strings.isNullOrEmpty(currPage))
            currPage = "1";
        if(Strings.isNullOrEmpty(pageSize))
            pageSize = "10";
        Page<ReportJobInfo> pageData = recordProcessDao.pageJob(new Integer(currPage),new Integer(pageSize),user_id);
        PageResult pageResult = PageResult.pageHelperList2PageResult(pageData);
        logger.debug("Page Result :{}",pageResult);
        return pageResult;
    }

    @Override
    public JobConfig getJobConfigByReportId(String reportId) {
        ReportJobInfo reportJobInfo = recordProcessDao.getReportJobInfoByReportId(reportId);
        JobConfig jobConfigEntity = recordProcessDao.getJobConfigByJobId(reportJobInfo.getJob_id().toString());

        return jobConfigEntity;
    }

    @Override
    public List<ReportFldConfig> getFldByUnitId(String unitId) {
        List<ReportFldConfig> unitFlds = recordProcessDao.getFldByUnitId(unitId);
        return unitFlds;
    }

    @Override
    public List<ReportJobData> getFldReportDatas(String jobId,String reportId, String groupId) {
        List<ReportJobData> result = recordProcessDao.getReportDataByUnitId(jobId,reportId,groupId);
        return result;
    }

    @Override
    public Map<Integer, List<DataDictionary>> getUnitDictFldContent(String groupId) {
        Map<Integer,List<DataDictionary>> result = new HashMap<>();

        List<ReportFldConfig> unitFlds = recordProcessDao.getFldByUnitId(groupId);
        if(unitFlds!=null&&unitFlds.size()>0){
            for (ReportFldConfig unitFld : unitFlds) {
                String fldDataType = unitFld.getFld_data_type();
                if(FldDataTypes.DICT.compareTo(fldDataType)){
                    List<DataDictionary> dictContext = recordProcessDao.getDictcontent4Fld(unitFld.getFld_id());
                    result.put(unitFld.getFld_id(),dictContext);
                }
            }
        }
        return result;
    }
}
