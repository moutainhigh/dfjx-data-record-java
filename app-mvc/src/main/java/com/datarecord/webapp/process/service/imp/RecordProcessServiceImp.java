package com.datarecord.webapp.process.service.imp;

import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.dataindex.bean.FldDataTypes;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.page.PageResult;
import com.workbench.exception.runtime.WorkbenchRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveGridDatas(ReportJobInfo reportJobInfo) {
        List<ReportJobData> reportJobDataList = reportJobInfo.getReportJobDataList();
        if(reportJobDataList!=null&&reportJobDataList.size()>0){
            Integer unitId = reportJobDataList.get(0).getUnit_id();
            recordProcessDao.deleteRecordDataByUnit(reportJobInfo.getJob_id(),reportJobInfo.getReport_id(),unitId);
        }
        for (ReportJobData reportJobData : reportJobDataList) {
            recordProcessDao.createRcdReortJobData(reportJobData,String.valueOf(reportJobInfo.getJob_id()));
        }
    }

    @Override
    public Map<Integer, Map<Integer, String>> validateGridDatas(List<ReportJobData> reportJobDataList, String unitId) {

//        Map<Integer,List<FldValidateFailBean>> validateResult = new HashMap<>();
        Map<Integer,Map<Integer,String>> validateResultMap = new HashMap<>();

        if(Strings.isNullOrEmpty(unitId)){
            throw new WorkbenchRuntimeException("填报组id为空",new Exception("填报组id为空"));
        }

        Map<Integer,ReportFldConfig> fldConfigMapCache = new HashMap();
        List<ReportFldConfig> flds4Unit = recordProcessDao.getFldByUnitId(unitId);
        if(flds4Unit!=null&&flds4Unit.size()>0){
            for (ReportFldConfig reportFldConfig : flds4Unit) {
                fldConfigMapCache.put(reportFldConfig.getFld_id(),reportFldConfig);
            }
        }

        if(reportJobDataList!=null&&reportJobDataList.size()>0){
            for (ReportJobData reportJobData : reportJobDataList) {
                Integer fldId = reportJobData.getFld_id();
                Integer columId = reportJobData.getColum_id();
                String reportData = reportJobData.getRecord_data();
                if(fldConfigMapCache.containsKey(fldId)){
                    ReportFldConfig fldConfig = fldConfigMapCache.get(fldId);
                    Integer allowedNullOrNot = fldConfig.getFld_is_null();
                    String fldDataType = fldConfig.getFld_data_type();
                    if(allowedNullOrNot!=0){
                        //校验是否可为空
                        if(Strings.isNullOrEmpty(reportData)){

                            if(!validateResultMap.containsKey(columId)){
                                validateResultMap.put(columId,new HashMap<>());
                            }
                            validateResultMap.get(columId).put(fldId,"不允许为空");

                        }
                        //校验数据格式是否符合
                        if(FldDataTypes.STRING.compareTo(fldDataType)){//字符串类型

                        }else if(FldDataTypes.DATE.compareTo(fldDataType)){//日期类型

                        }else if(FldDataTypes.DICT.compareTo(fldDataType)){//字典类型

                        }else if(FldDataTypes.NUMBER.compareTo(fldDataType)){//数字类型
                            try{
                                Integer dataInt = new Integer(reportData);
                                BigDecimal dataBig = new BigDecimal(reportData);

                            }catch (NumberFormatException e){
                                try{
                                    Long dataFormatter = new Long(reportData);
                                }catch (NumberFormatException e1){
                                    try{
                                        Float dataFormatter = new Float(reportData);
                                    }catch(NumberFormatException e2){
                                        try{
                                            Double dataFormatter = new Double(reportData);
                                        }catch(NumberFormatException e3){
                                            try{
                                                BigDecimal dataFormatter = new BigDecimal(reportData);
                                            }catch(NumberFormatException e4){
                                                if(!validateResultMap.containsKey(columId)){
                                                    validateResultMap.put(columId,new HashMap<>());
                                                }
                                                validateResultMap.get(columId).put(fldId,"数字格式错误");

                                            }

                                        }
                                    }
                                }
                            }
                        }else{
                            throw new WorkbenchRuntimeException("未找到对应的数据类型",new Exception("未找到对应的数据类型"));

                        }

                    }
                }else{
                    throw new WorkbenchRuntimeException("填报项"+fldId+"找不到定义",new Exception("填报项"+fldId+"找不到定义"));
                }
            }
            


        }else{
            throw new WorkbenchRuntimeException("填报数据为空",new Exception("填报数据为空"));
        }

        return validateResultMap;

    }
}
