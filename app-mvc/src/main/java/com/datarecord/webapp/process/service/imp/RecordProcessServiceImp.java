package com.datarecord.webapp.process.service.imp;

import com.datarecord.webapp.dataindex.bean.RcdDt;
import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
            public void run() {
                logger.info("开始发布填报任务:{}",jobConfigEntity.getJob_name());
                recordProcessDao.changeRecordJobStatus(jobId, JobConfigStatus.SUBMITING.value() );

                List<JobUnitConfig> jobUnits = jobConfigEntity.getJobUnits();
                for (JobUnitConfig jobUnit : jobUnits) {
                    List<RcdDt> unitFlds = jobUnit.getUnitFlds();
                    List<JobPerson> allJobPerson = jobConfigEntity.getJobPersons();
                    RcdReportJobEntity rcdReportJobEntity = new RcdReportJobEntity();
                    rcdReportJobEntity.setUnit_id(jobUnit.getJob_unit_id());
                    rcdReportJobEntity.setColum_id(0);
                    rcdReportJobEntity.setRecord_data("预设值");
                    for (RcdDt unitFld : unitFlds) {
                        int fldId = unitFld.getFld_id();
                        rcdReportJobEntity.setFld_id(fldId);
                        for (JobPerson jobPerson : allJobPerson) {
                            Integer originId = jobPerson.getOrigin_id();
                            Integer userId = jobPerson.getUser_id();
                            rcdReportJobEntity.setRecord_origin_id(originId);
                            rcdReportJobEntity.setRecord_user_id(userId);
                            recordProcessDao.createRcdReortJobData(rcdReportJobEntity,jobId);
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
}
