package com.datarecord.webapp.process.service.imp;

import com.datarecord.webapp.fillinatask.bean.JobUnitAcitve;
import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordMaker;
import com.webapp.support.jsonp.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("recordMaker")
public class RecordMakerImp implements RecordMaker {

    private Logger logger = LoggerFactory.getLogger(RecordMakerImp.class);

    @Autowired
    protected IRecordProcessDao recordProcessDao;

    private boolean debugger = true;

    @Override
    public Map<JsonResult.RESULT, Object> makeJob(String jobId) {
        JobConfig jobConfigEntity = recordProcessDao.getJobConfigByJobId(jobId);

        Map<JsonResult.RESULT, Object> preMakeResult = this.preMake(jobConfigEntity);
        if(preMakeResult!=null&&preMakeResult.containsKey(JsonResult.RESULT.FAILD)){
            return preMakeResult;
        }

        Map<JsonResult.RESULT,Object> makeResultMap = new HashMap<>();
        logger.info("job config entity : {}",jobConfigEntity);
        List<JobPerson> allJobPerson = jobConfigEntity.getJobPersons();
        List<JobUnitConfig> jobUnits = jobConfigEntity.getJobUnits();

        //根据任务建表,每个对应一张表
        recordProcessDao.dropJobDataTable(jobId);
        recordProcessDao.makeJobDataTable(jobId);

        //根据任务对应的填报人和填报单位,下发任务

        String debuggerMainThreadWait = "";

        new Thread(new Runnable() {
            @Override
            @Transactional(rollbackFor = Exception.class)
            public void run() {
                logger.info("开始发布填报任务:{}",jobConfigEntity.getJob_name());
                recordProcessDao.changeRecordJobConfigStatus(jobId, JobConfigStatus.SUBMITING.value() );

                //循环为每个填报人生成任务
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

                    for (JobUnitConfig jobUnit : jobUnits) {
                        if(jobUnit.getJob_unit_active() == JobUnitAcitve.UNACTIVE.value()){
                            continue;
                        }

                        List<ReportFldConfig> unitFlds = jobUnit.getUnitFlds();
                        for (ReportFldConfig unitFld : unitFlds) {
                            int fldId = unitFld.getFld_id();
                            ReportJobData reportJobData = new ReportJobData();
                            reportJobData.setColum_id(0);
                            reportJobData.setFld_id(fldId);
                            reportJobData.setReport_id(reportId);
                            reportJobData.setUnit_id(jobUnit.getJob_unit_id());
                            reportJobData.setRecord_data("");
                            recordProcessDao.createRcdReortJobData(reportJobData,jobId);
                        }
                    }
                }

                logger.info("填报任务发布完成:{}",jobConfigEntity.getJob_name());
                recordProcessDao.changeRecordJobConfigStatus(jobId, JobConfigStatus.SUBMIT.value());
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

        return makeResultMap;
    }

    @Override
    public Map<JsonResult.RESULT, Object> preMake(JobConfig jobConfigEntity) {

        Map<JsonResult.RESULT, Object> checkPersonResult = this.checkReportPersons(jobConfigEntity);
        if(checkPersonResult.containsKey(JsonResult.RESULT.FAILD)){
            return checkPersonResult;
        }

        Map<JsonResult.RESULT, Object> checkConfigResult = this.checkJobConfig(jobConfigEntity);
        if(checkConfigResult.containsKey(JsonResult.RESULT.FAILD)){
            return checkConfigResult;
        }

        Map<JsonResult.RESULT, Object> checkFlowItemsResult = this.checkFlowItems(jobConfigEntity);
        if(checkFlowItemsResult.containsKey(JsonResult.RESULT.FAILD)){
            return checkFlowItemsResult;
        }

        return null;
    }

    /**
     * 检查填报人是否设置是否合法
     */
    private Map<JsonResult.RESULT, Object> checkReportPersons(JobConfig jobConfigEntity){
        Map<JsonResult.RESULT,Object> makeResultMap = new HashMap<>();
        List<JobPerson> allJobPerson = jobConfigEntity.getJobPersons();
        if(!(allJobPerson!=null&&allJobPerson.size()>0)){
            makeResultMap.put(JsonResult.RESULT.FAILD,"任务没有关联的填报人");
            return makeResultMap;
        }
        return makeResultMap;
    }


    private Map<JsonResult.RESULT, Object> checkJobConfig(JobConfig jobConfigEntity){
        Map<JsonResult.RESULT,Object> makeResultMap = new HashMap<>();
        List<JobUnitConfig> jobUnits = jobConfigEntity.getJobUnits();
        if(!(jobUnits!=null&&jobUnits.size()>0)){
            makeResultMap.put(JsonResult.RESULT.FAILD,"任务没有关联的任务组");
            return makeResultMap;
        }else{
            int activeUnitCount = 0;
            for (JobUnitConfig jobUnit : jobUnits) {
                if(jobUnit.getJob_unit_active() == JobUnitAcitve.ACTIVE.value()){
                    activeUnitCount++;
                    List<ReportFldConfig> unitFlds = jobUnit.getUnitFlds();
                    if(!(unitFlds!=null&&unitFlds.size()>0)){
                        makeResultMap.put(JsonResult.RESULT.FAILD,"任务组"+jobUnit.getJob_unit_name()+"下没有定义指标");
                        return makeResultMap;
                    }
                }
            }
            if(activeUnitCount==0){
                makeResultMap.put(JsonResult.RESULT.FAILD,"任务没有生效的任务组");
                return makeResultMap;
            }
        }
        return makeResultMap;
    }

    /**
     * 检查是否有不符合流程的配置
     */
    private Map<JsonResult.RESULT, Object> checkFlowItems(JobConfig jobConfigEntity){
        Map<JsonResult.RESULT,Object> makeResultMap = new HashMap<>();
        List<JobUnitConfig> jobUnits = jobConfigEntity.getJobUnits();
        List<String> faildList = new ArrayList<>();
        for (JobUnitConfig jobUnit : jobUnits) {
            List<ReportFldConfig> unitFlds = jobUnit.getUnitFlds();
            for (ReportFldConfig unitFld : unitFlds) {
                Integer isActive = unitFld.getIs_actived();
                Integer fldStatus = unitFld.getFld_status();
                if(isActive==0){//填报指标未经审批
                    faildList.add(new StringBuilder().append(jobUnit.getJob_unit_active()).append(":").append(unitFld.getFld_name()).append("未启用").toString());
                    continue;
                }
                if(fldStatus==0){
                    faildList.add(new StringBuilder().append(jobUnit.getJob_unit_active()).append(":").append(unitFld.getFld_name()).append("待审批").toString());
                    continue;
                }
                if(fldStatus==2){
                    faildList.add(new StringBuilder().append(jobUnit.getJob_unit_active()).append(":").append(unitFld.getFld_name()).append("审批被驳回").toString());
                    continue;
                }
                if(fldStatus==3){
                    faildList.add(new StringBuilder().append(jobUnit.getJob_unit_active()).append(":").append(unitFld.getFld_name()).append("已作废").toString());
                    continue;
                }
            }
        }
        if(faildList!=null&&faildList.size()>0){
            makeResultMap.put(JsonResult.RESULT.FAILD,faildList);
        }
        
        return makeResultMap;
    }
}
