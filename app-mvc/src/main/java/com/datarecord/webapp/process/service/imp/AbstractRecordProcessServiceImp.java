package com.datarecord.webapp.process.service.imp;

import com.datarecord.webapp.dataindex.bean.RcdClientType;
import com.datarecord.webapp.fillinatask.bean.JobUnitAcitve;
import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.dataindex.bean.FldDataTypes;
import com.datarecord.webapp.reportinggroup.bean.RcdJobUnitFlow;
import com.datarecord.webapp.reportinggroup.bean.ReportGroupInterval;
import com.datarecord.webapp.reportinggroup.dao.ReportingGroupDao;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.workbench.exception.runtime.WorkbenchRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

@Service("recordProcessService")
public class AbstractRecordProcessServiceImp implements RecordProcessService {

    private Logger logger = LoggerFactory.getLogger(AbstractRecordProcessServiceImp.class);

    private boolean debugger = true;

    @Autowired
    protected IRecordProcessDao recordProcessDao;

    @Autowired
    private ReportingGroupDao reportingGroupDao;

    @Override
    public Map<JsonResult.RESULT, String> makeJob(String jobId) {
        JobConfig jobConfigEntity = recordProcessDao.getJobConfigByJobId(jobId);
        Map<JsonResult.RESULT,String> makeResultMap = new HashMap<>();
        logger.info("job config entity : {}",jobConfigEntity);
        List<JobPerson> allJobPerson = jobConfigEntity.getJobPersons();
        List<JobUnitConfig> jobUnits = jobConfigEntity.getJobUnits();
        if(!(allJobPerson!=null&&allJobPerson.size()>0)){
            makeResultMap.put(JsonResult.RESULT.FAILD,"任务没有关联的填报人");
            return makeResultMap;
        }

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
    public PageResult pageJob(int user_id, String currPage, String pageSize,Map<String,String> queryParams) {
        if(Strings.isNullOrEmpty(currPage))
            currPage = "1";
        if(Strings.isNullOrEmpty(pageSize))
            pageSize = "10";
        Page<ReportJobInfo> pageData = recordProcessDao.pageJob(new Integer(currPage),new Integer(pageSize),user_id,queryParams);
        PageResult pageResult = PageResult.pageHelperList2PageResult(pageData);
        List<ReportJobInfo> dataList = pageResult.getDataList();
        Date currDate = new Date();

        for (ReportJobInfo reportCustomer : dataList) {
            Date startDate = reportCustomer.getJob_start_dt();
            Date endDate = reportCustomer.getJob_end_dt();

            if(currDate.compareTo(startDate)<0){//未到填报日期
                reportCustomer.setRecord_status(ReportStatus.TOO_EARLY.getValueInteger());
            }
            if(currDate.compareTo(endDate)>0){//已过期
                reportCustomer.setRecord_status(ReportStatus.OVER_TIME.getValueInteger());
                recordProcessDao.changeRecordJobStatus(reportCustomer.getReport_id(),ReportStatus.OVER_TIME.getValueInteger());
            }
        }

        logger.debug("Page Result :{}",pageResult);
        return pageResult;
    }

    @Override
    public JobConfig getJobConfigByReportId(String reportId) {
        ReportJobInfo reportJobInfo = recordProcessDao.getReportJobInfoByReportId(reportId);
        JobConfig jobConfigEntity = recordProcessDao.getJobConfigByJobId(reportJobInfo.getJob_id().toString());

        for (JobUnitConfig jobUnit : jobConfigEntity.getJobUnits()) {
            RcdJobUnitFlow unitFlow = reportingGroupDao.getUnitFLow(jobUnit.getJob_id(),String.valueOf(jobUnit.getJob_unit_id()));
            List<ReportGroupInterval> reportGroupIntervals = reportingGroupDao.getReportGroupInterval(jobUnit.getJob_id(),jobUnit.getJob_unit_id());
            jobUnit.setReportGroupIntervals(reportGroupIntervals);
            jobUnit.setRcdJobUnitFlow(unitFlow);
        }

        return jobConfigEntity;
    }

    @Override
    public List<ReportFldTypeConfig> getFldByUnitId(String unitId) {
        List<ReportFldConfig> unitFlds = recordProcessDao.getFldByUnitId(unitId);

        ArrayList<ReportFldTypeConfig> catgFlds = this.groupFLdCatge(unitFlds);

        return catgFlds;
    }

    private ArrayList<ReportFldTypeConfig> groupFLdCatge(List<ReportFldConfig> unitFlds ){
        Map<Integer,ReportFldTypeConfig> catgFldMapTmp = new HashMap<>();
        for (ReportFldConfig unitFld : unitFlds) {
            Integer catgId = unitFld.getCatg_id();
            if(!catgFldMapTmp.containsKey(catgId)){
                ReportFldTypeConfig reportFldTypeConfig = new ReportFldTypeConfig();
                reportFldTypeConfig.setCatg_id(catgId);
                reportFldTypeConfig.setCatg_name(unitFld.getCatg_name());
                reportFldTypeConfig.setUnitFlds(new ArrayList<>());
                catgFldMapTmp.put(catgId,reportFldTypeConfig);
            }
            catgFldMapTmp.get(catgId).getUnitFlds().add(unitFld);
        }

        return new ArrayList<>(catgFldMapTmp.values());
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
    //子类实现
    public void saveDatas(SaveReportJobInfos reportJobInfo) {
    }

    @Override
    //子类实现
    public Map<Integer, Map<Integer, String>> validateDatas(List<ReportJobData> reportJobDataList, String unitId,String clientType) {
        if(reportJobDataList!=null&&reportJobDataList.size()>0){
            if(Strings.isNullOrEmpty(unitId)){
                throw new WorkbenchRuntimeException("填报组id为空",new Exception("填报组id为空"));
            }

            Map<Integer, ReportFldConfig> fldConfigMapCache = this.getReportFldConfigMap(unitId);

            Map<Integer,Map<Integer,String>> validateResultMap = this.checkReportData(reportJobDataList,fldConfigMapCache,
                    RcdClientType.MOBILE.toString().equals(clientType)?RcdClientType.MOBILE:RcdClientType.PC);

            return validateResultMap;
        }
        return null;
    }

    @Override
    public List<ReportFldTypeConfig> getClientFldByUnitId(String groupId, String clientType) {
        if(Strings.isNullOrEmpty(clientType)){
            return null;
        }else{
            List<ReportFldConfig> allGroupFlds = recordProcessDao.getFldByUnitId(groupId);
            List<ReportFldConfig> pcGroupFlds = new ArrayList<>();
            List<ReportFldConfig> mobileGroupFlds = new ArrayList<>();
            for (ReportFldConfig groupFld : allGroupFlds) {
                Integer fldRange = groupFld.getFld_range();//哪个客户端填写：0-所有、1-移动端、2-PC端.
                Integer fldVisible = groupFld.getFld_visible();//哪个客户端需要显示：0-所有、1-移动端、2-PC端.

                if(RcdClientType.ALL.getValue()==fldRange){
                    //do nothing
                    pcGroupFlds.add(groupFld);
                    mobileGroupFlds.add(groupFld);
                }else if(RcdClientType.PC.getValue()==fldRange){//PC端填写的数据
                    pcGroupFlds.add(groupFld);
                    if(RcdClientType.PC.getValue()!=fldVisible){
                        mobileGroupFlds.add(groupFld);
                    }
                }
                else if(RcdClientType.MOBILE.getValue()==fldRange){
                    mobileGroupFlds.add(groupFld);
                    if(RcdClientType.MOBILE.getValue()!=fldVisible){
                        pcGroupFlds.add(groupFld);
                    }
                }else{

                }
            }

            if(RcdClientType.PC.toString().equals(clientType)){
                List<ReportFldTypeConfig> pcGroupFldList = this.groupFLdCatge(pcGroupFlds);
                return pcGroupFldList;
            }else if (RcdClientType.MOBILE.toString().equals(clientType)){
                List<ReportFldTypeConfig> pcGroupFldList = this.groupFLdCatge(pcGroupFlds);
                return pcGroupFldList;
            }else{
                return null;
            }
        }
    }

    @Override
    public void updateReportStatus(String reportId, ReportStatus reportStatus) {
        recordProcessDao.updateReportStatus(reportId,reportStatus.getValue());
    }

    protected Map<Integer,ReportFldConfig> getReportFldConfigMap(String unitId){
        Map<Integer, ReportFldConfig> fldConfigMapCache = new HashMap();
        List<ReportFldConfig> flds4Unit = recordProcessDao.getFldByUnitId(unitId);
        if(flds4Unit!=null&&flds4Unit.size()>0){
            for (ReportFldConfig reportFldConfig : flds4Unit) {
                fldConfigMapCache.put(reportFldConfig.getFld_id(),reportFldConfig);
            }
        }
        return fldConfigMapCache;
    }

    protected Map<Integer, Map<Integer, String>> checkReportData(List<ReportJobData> reportJobDataList, Map<Integer, ReportFldConfig> fldConfigMap,RcdClientType rcdClientType){
        Map<Integer,Map<Integer,String>> validateResultMap = new HashMap<>();
        if(reportJobDataList!=null&&reportJobDataList.size()>0){
            for (ReportJobData reportJobData : reportJobDataList) {
                Integer fldId = reportJobData.getFld_id();
                Integer columId = reportJobData.getColum_id();
                String reportData = reportJobData.getRecord_data();
                if(fldConfigMap.containsKey(fldId)){
                    ReportFldConfig fldConfig = fldConfigMap.get(fldId);

                    Integer fldRange = fldConfig.getFld_range();//数据填报端 0:所有 1：移动 2：PC
                    if(fldRange!=RcdClientType.ALL.getValue()&&rcdClientType.getValue()!=fldRange){
                        continue;
                    }

                    Integer allowedNullOrNot = fldConfig.getFld_is_null();
                    String fldDataType = fldConfig.getFld_data_type();
                    if(allowedNullOrNot!=0){//不允许为空
                        //校验是否可为空
                        if(Strings.isNullOrEmpty(reportData)){
                            if(!validateResultMap.containsKey(columId)){
                                validateResultMap.put(columId,new HashMap<>());
                            }
                            validateResultMap.get(columId).put(fldId,"不允许为空");
                            continue;
                        }
                    }else{//允许为空
                        if(Strings.isNullOrEmpty(reportData)){
                            continue;
                        }
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
                }else{
                    throw new WorkbenchRuntimeException("填报项"+fldId+"找不到定义",new Exception("填报项"+fldId+"找不到定义"));
                }
            }
        }
        return validateResultMap;

    }
}
