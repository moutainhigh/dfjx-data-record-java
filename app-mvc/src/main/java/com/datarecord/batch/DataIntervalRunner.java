package com.datarecord.batch;

import com.datarecord.enums.JobUnitType;
import com.datarecord.enums.ReportFldStatus;
import com.datarecord.enums.ReportStatus;
import com.datarecord.webapp.fillinatask.bean.JobInteval;
import com.datarecord.webapp.fillinatask.bean.JobUnitAcitve;
import com.datarecord.webapp.fillinatask.service.JobConfigService;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobUnitConfig;
import com.datarecord.webapp.process.entity.ReportJobInfo;
import com.datarecord.webapp.process.service.RecordMaker;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.google.common.base.Strings;
import com.webapp.support.utils.DateSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service("dataIntervalRunner")
public class DataIntervalRunner  {

    @Autowired
    private RecordMaker recordMaker;

    @Autowired
    private RecordProcessService recordProcessService;

    @Autowired
    private JobConfigService jobConfigService;

    @Autowired
    private DataSourceTransactionManager transactionManager;

    private static Logger logger = LoggerFactory.getLogger(DataIntervalRunner.class);

    private String INNER_JOBS = "INNER_JOBS";
    private String OUTTER_JOBS = "OUTTER_JOBS";

    @PostConstruct
    public void initialize(){
        this.WATCHER();
    }

    @Transactional(rollbackFor = Exception.class)
    protected void RUNNER(){
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        TransactionStatus status = transactionManager.getTransaction(def); // 获得事务状态

        try{
            Map<String, List<Integer>> checkResult = checkJobs();
            List<Integer> innerJobList = checkResult.get(INNER_JOBS);
            List<Integer> outterJobList = checkResult.get(OUTTER_JOBS);
            //检查是否有关闭和重新开启都有的任务
            List<Integer> finalOutterJobList = new ArrayList<>();
            if(outterJobList!=null&&outterJobList.size()>0){
                for (Integer outterJobId : outterJobList) {
                    if(!innerJobList.contains(outterJobId)){
                        finalOutterJobList.add(outterJobId);
                    }
                }
            }

            for (Integer jobId : finalOutterJobList) {
                this.closeOutterData(jobId);
            }
            for (Integer jobId : innerJobList) {
                this.makeNewRecord(jobId);
            }


            transactionManager.commit(status);
        }catch(Exception e){
            e.printStackTrace();
            transactionManager.rollback(status);
        }
        
    }

    private void WATCHER(){
        Thread runnable = new Thread(() -> {
            RUNNER();
        }, "DataInTervalRunner");
        long remainingTime = DateSupport.getRemainingTime(DateSupport.Beijing_ShangHai_TimeZone());
//        remainingTime = 30000;

        logger.info("建立任务填报周期监听线程，线程将在{}秒后开始执行，并且在首次执行后，每隔24小时执行一次",
                remainingTime/1000);


        ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        scheduledExecutor.scheduleAtFixedRate(
                runnable,
                remainingTime,
//                60*60*24*1000,
                (long) (60*60*(0.5)*1000),
                TimeUnit.MILLISECONDS);
    }

    @Transactional(rollbackFor = Exception.class)
    void makeNewRecord(Integer jobId){
        List<ReportJobInfo> reportJobInfos = recordProcessService.getReportJobInfosByJobId(jobId.toString());
        if(reportJobInfos!=null&&reportJobInfos.size()>0){
            JobConfig jobConfig = recordProcessService.getJobConfigByJobId(jobId.toString());
            recordProcessService.changeJobDataStatus(
                    jobConfig.getJob_id().toString(),
                    ReportFldStatus.NORMAL.getValueInteger());
            reportJobInfos.forEach(reportJobInfo->{
                Integer reportId = reportJobInfo.getReport_id();
                for (JobUnitConfig jobUnit : jobConfig.getJobUnits()) {
                    logger.info("为填报任务:任务ID：{}-数据组:{}自动生成数据",jobUnit.getJob_id(),jobUnit.getJob_unit_name());
                    if(jobUnit.getJob_unit_active() == JobUnitAcitve.UNACTIVE.value()){
                        logger.info("为填报任务:任务ID：{}-数据组:{}自动生成数据--->失败，原因为当前填报数据组未开启",jobUnit.getJob_id(),jobUnit.getJob_unit_name());
                        continue;
                    }
                    recordProcessService.updateReportStatus(reportId.toString(), ReportStatus.NORMAL);
                    if(jobUnit.getJob_unit_type() == JobUnitType.GRID.value()){
                        Integer maxColumId = recordProcessService.getMaxColumId(jobId.toString(),reportId.toString(),jobUnit.getJob_unit_id().toString());
                        recordMaker.createGridRecordDatas(jobUnit,reportId,maxColumId+1);
                    }
                    if(jobUnit.getJob_unit_type() == JobUnitType.SIMPLE.value()){
                        //TO BE CONTINUE!!!!!!!!!!!!!!!!
                    }
                    logger.info("为填报任务:任务ID：{}-数据组:{}自动生成数据--->完成",jobUnit.getJob_id(),jobUnit.getJob_unit_name());
                }
            });
        }else {
            logger.error("当前任务id：{}，不存在任何填报数据",jobId);
            return;
        }
    }

    @Transactional(rollbackFor = Exception.class)
    protected void closeOutterData(Integer jobId){
        List<ReportJobInfo> reportJobInfos = recordProcessService.getReportJobInfosByJobId(jobId.toString());
        reportJobInfos.forEach(reportJobInfo->{
            Integer reportId = reportJobInfo.getReport_id();
            Integer recordStatus = reportJobInfo.getRecord_status();
            if(recordStatus == ReportStatus.NORMAL.getValueInteger()){
                logger.info("当前填报任务:{},状态为：{},将其置位未提交状态",reportJobInfo.getJob_name(),reportJobInfo.getRecord_status_cn());
                recordProcessService.doCommitAuth(reportId.toString(), ReportFldStatus.UNSUB);
            }else{
                logger.info("当前填报任务:{},状态为：{},不做未提交处理",reportJobInfo.getJob_name(),reportJobInfo.getRecord_status_cn());
            }
        });
    }

    public Map<String,List<Integer>> checkJobs(){
        logger.info("开始检查当前任务是否开始填报，是否已经超出填报时间范围");
        
        Map<String,List<Integer>> resultJobs = new HashMap<>();
            resultJobs.put(INNER_JOBS,new ArrayList());
        resultJobs.put(OUTTER_JOBS,new ArrayList());
        Map<Integer, List<JobInteval>> allJobIntevals = jobConfigService.allJobIntevals();
        logger.info("获取所有填报区间数据完成");

        Calendar bjCalendar = DateSupport.getBeijingCalendar();
        int today = bjCalendar.get(Calendar.DAY_OF_MONTH);
        bjCalendar.add(Calendar.DAY_OF_MONTH,-1);
        int yesterday = bjCalendar.get(Calendar.DAY_OF_MONTH);
        logger.info("开始递归所有填报区间数据");
        if(allJobIntevals!=null&&allJobIntevals.size()>0){
            for (Integer jobId : allJobIntevals.keySet()) {
                List<JobInteval> jobIntevals = allJobIntevals.get(jobId);
                if(jobIntevals!=null&&jobIntevals.size()>0){
                    for (JobInteval jobInteval : jobIntevals) {
                        String startDate = jobInteval.getJob_interval_start();
                        String endDate = jobInteval.getJob_interval_end();
                        if(!Strings.isNullOrEmpty(startDate)){
                            if(new Integer(startDate)==today){
                                resultJobs.get(INNER_JOBS).add(jobInteval.getJob_id());
                            }
                        }
                        if(!Strings.isNullOrEmpty(endDate)){
                            if(new Integer(endDate) == yesterday){
                                resultJobs.get(OUTTER_JOBS).add(jobInteval.getJob_id());
                            }
                        }
                    }
                }
            }
        }
        logger.info("检查当前任务是否开始填报，是否已经超出填报时间范围----->完成,当天为开始日期的任务共计:{}个,昨日为最后一天填报日期的任务共计:{}个",
                resultJobs.get(INNER_JOBS).size(),
                resultJobs.get(OUTTER_JOBS).size());

        return resultJobs;
    }

}
