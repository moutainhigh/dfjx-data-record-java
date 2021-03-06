package com.datarecord.webapp.process.service.imp;

import com.datarecord.enums.*;
import com.datarecord.webapp.fillinatask.bean.JobInteval;
import com.datarecord.webapp.fillinatask.bean.UpDownLoadFileConfig;
import com.datarecord.webapp.fillinatask.service.JobConfigService;
import com.datarecord.webapp.process.RecordFileService;
import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.reportinggroup.bean.RcdJobUnitFlow;
import com.datarecord.webapp.reportinggroup.bean.ReportGroupInterval;
import com.datarecord.webapp.reportinggroup.dao.ReportingGroupDao;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.sys.origin.service.RecordOriginService;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.page.PageResult;
import com.workbench.exception.runtime.WorkbenchRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

@Service("recordProcessService")
public class AbstractRecordProcessServiceImp implements RecordProcessService {

    private Logger logger = LoggerFactory.getLogger(AbstractRecordProcessServiceImp.class);

    @Autowired
    protected IRecordProcessDao recordProcessDao;

    @Autowired
    private ReportingGroupDao reportingGroupDao;

    @Autowired
    private JobConfigService jobConfigService;

    @Autowired
    private RecordOriginService recordOriginService;

    @Autowired
    private OriginService originService;

    @Autowired
    private UpDownLoadFileConfig upDownLoadFileConfig;

    @Autowired
    private RecordFileService recordFileService;

    @Override
    public PageResult pageJob(BigInteger user_id, String currPage, String pageSize, Map<String,String> queryParams) {
        if(Strings.isNullOrEmpty(currPage))
            currPage = "1";
        if(Strings.isNullOrEmpty(pageSize))
            pageSize = "10";

        Page<ReportJobInfo> pageData = recordProcessDao.pageJob(new Integer(currPage),new Integer(pageSize),user_id,queryParams);


        PageResult pageResult = PageResult.pageHelperList2PageResult(pageData);


        this.checkReportStatus(pageResult.getDataList());


        logger.debug("Page Result :{}",pageResult);
        return pageResult;
    }

    @Override
    public PageResult pageAuthJobConfig(BigInteger userId, String currPage, String pageSize, Map<String, String> queryParams) {
        if(Strings.isNullOrEmpty(currPage))
            currPage = "1";
        if(Strings.isNullOrEmpty(pageSize))
            pageSize = "10";
        Page<JobConfig> aurhJobConfigPageData = recordProcessDao.pageAuthJobConfig(userId,currPage,pageSize,queryParams);

        PageResult pageResultData = PageResult.pageHelperList2PageResult(aurhJobConfigPageData);

        return pageResultData;
    }


    @Override
    public List<JobConfig> allAuthJobConfig(BigInteger userId, Map<String, String> queryParams) {
        List<JobConfig> aurhJobConfigPageData = recordProcessDao.pageAuthJobConfig(userId,null,null,queryParams);
        return aurhJobConfigPageData;
    }

    @Override
    public PageResult pageJobDataByJobId(BigInteger userId, String jobId, String currPage, String pageSize, Map<String, String> queryParams) {
        if(Strings.isNullOrEmpty(currPage))
            currPage = "1";
        if(Strings.isNullOrEmpty(pageSize))
            pageSize = "10";

        Page<ReportJobInfo> authJobDatas = recordProcessDao.pageJobDataByJobId(userId,currPage,pageSize,jobId,queryParams);

        if(authJobDatas!=null&&authJobDatas.size()>0){
            List<Origin> allOriginList = originService.listAllOrigin();
            for (ReportJobInfo authJobData : authJobDatas) {
                String originName = recordOriginService.addParentOriginName(authJobData.getRecord_origin_id().toString(), allOriginList);
                authJobData.setRecord_origin_name(originName);
            }
        }


        PageResult pageResultData = PageResult.pageHelperList2PageResult(authJobDatas);
        this.checkReportStatus(pageResultData.getDataList());

        return pageResultData;
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
    public JobConfig getJobConfigByJobId(String jobId) {
        JobConfig jobConfigEntity = recordProcessDao.getJobConfigByJobId(jobId);

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
    public PageResult pageFldReportDatas(String jobId, String reportId, String groupId, String currPage) {
        final Integer defaultPageSize = 30;//每页200条
        Integer dataCount = recordProcessDao.checkReportDataCount(jobId, reportId, groupId);
        Integer totalPageSize = 1;
        if(dataCount>0){
            totalPageSize = dataCount/defaultPageSize;
            if(dataCount%defaultPageSize>0){
                totalPageSize++;
            }
        }
        Integer currPagePosition = 0;
        Integer currPageInteger = 0;
        if(Strings.isNullOrEmpty(currPage)){
            currPageInteger = totalPageSize;
        }else{
            currPageInteger = new Integer(currPage);
        }
        if(currPageInteger==1){
            currPagePosition = (currPageInteger-1)*defaultPageSize;
        }else{
            currPagePosition = (currPageInteger-1)*defaultPageSize-1;
        }

        List<ReportJobData> result = recordProcessDao.pageReportDataByUnitId(jobId,reportId,groupId,currPagePosition,defaultPageSize);
        PageResult pageResult = new PageResult();
        pageResult.setCurrPage(currPageInteger);
        pageResult.setDataList(result);
        pageResult.setPageSize(defaultPageSize);
        pageResult.setTotalPage(totalPageSize);
        return pageResult;
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
                List<ReportFldTypeConfig> mobileGroupFldList = this.groupFLdCatge(mobileGroupFlds);
                return mobileGroupFldList;
            }else{
                List<ReportFldTypeConfig> allFldList = new ArrayList();
                allFldList.addAll(this.groupFLdCatge(pcGroupFlds));
                allFldList.addAll(this.groupFLdCatge(mobileGroupFlds));
                return allFldList;
            }
        }
    }

    @Override
    public void updateReportStatus(String reportId, ReportStatus reportStatus) {
        recordProcessDao.updateReportStatus(reportId,reportStatus.getValue());
    }

    @Override
    public ReportJobInfo getReportJobInfo(String reportId) {
        ReportJobInfo reportJobInfo = recordProcessDao.getReportJobInfoByReportId(reportId);
        return reportJobInfo;
    }

    @Override
    public List<ReportJobInfo> getReportJobInfosByJobId(String jobId) {
        return recordProcessDao.getReportJobInfosByJobId(jobId);
    }

    @Override
    public List<ReportJobInfo> checkReportStatus(List<ReportJobInfo> dataList) {
        Date currDate = new Date();
        Calendar calenday = Calendar.getInstance();
        calenday.setTime(currDate);
        calenday.set(Calendar.HOUR_OF_DAY, 0);
        calenday.set(Calendar.MINUTE, 0);
        calenday.set(Calendar.SECOND, 0);
        calenday.set(Calendar.MILLISECOND, 0);

        int dayOfMonth = calenday.get(Calendar.DAY_OF_MONTH);
        for (ReportJobInfo reportCustomer : dataList) {
            Integer jobId = reportCustomer.getJob_id();
            if(ReportStatus.REPORT_DONE.compareTo(reportCustomer.getRecord_status())){
                continue;
            }

            List<JobInteval> jobIntervals = jobConfigService.getJobIntevals(jobId.toString());

            boolean inner = false;
            if(jobIntervals!=null&&jobIntervals.size()>0){
                for (JobInteval jobInterval : jobIntervals) {
                    String startDay = jobInterval.getJob_interval_start();
                    String endDay = jobInterval.getJob_interval_end();
                    Integer startDayInt = new Integer(startDay);
                    Integer endDayInt = new Integer(endDay);
                    if(dayOfMonth>=startDayInt&&dayOfMonth<=endDayInt){
                        inner = true;
                    }
                }
            }else{
                inner = true;
            }
            if(!inner){
                reportCustomer.setRecord_status(ReportStatus.OVER_INTERVAL.getValueInteger());
            }

            Date startDate = reportCustomer.getJob_start_dt();
            Date endDate = reportCustomer.getJob_end_dt();

            if(calenday.getTime().compareTo(startDate)<0){//未到填报日期
                reportCustomer.setRecord_status(ReportStatus.TOO_EARLY.getValueInteger());
            }

            if(calenday.getTime().compareTo(endDate)>0){//已过期pageJob
                reportCustomer.setRecord_status(ReportStatus.OVER_TIME.getValueInteger());
                recordProcessDao.changeRecordJobStatus(reportCustomer.getReport_id(),ReportStatus.OVER_TIME.getValueInteger());
            }

        }

        return dataList;
    }

    @Override
    public List<DataDictionary> getDictcontent4Fld(Integer fld_id) {
        return recordProcessDao.getDictcontent4Fld(fld_id);
    }

    @Override
    public List<ReportJobData> getUnitDatas(String jobId, String reportId, String unitId) {

        return recordProcessDao.getUnitDatas(jobId,reportId,unitId);
    }

    @Override
    public List<Integer> getUnitColums(String jobId, String reportId, String unitId) {
        return recordProcessDao.getUnitColums(jobId,reportId,unitId);
    }

    @Override
    public List<Map<Integer, Map<Integer, String>>> validatePcReport(String reportId) {
        ReportJobInfo reportJobInfo = recordProcessDao.getReportJobInfoByReportId(reportId);
        List<JobUnitConfig> reportUnits = recordProcessDao.getJobUnitsByJobId(reportJobInfo.getJob_id().toString());
        List<Map<Integer, Map<Integer, String>>> unitValidateResults = new ArrayList<>();

        reportUnits.forEach(reportUnit->{
            Integer unitId = reportUnit.getJob_unit_id();
            List<ReportJobData> reportDatas = recordProcessDao.getReportDataByUnitId(
                    reportJobInfo.getJob_id().toString(),
                    reportJobInfo.getReport_id().toString(),
                    unitId.toString());
            Map<Integer, Map<Integer, String>> unitValidateResult = this.validateDatas(reportDatas, unitId.toString(), RcdClientType.PC.toString());
            if(unitValidateResult!=null&&unitValidateResult.size()>0){
                unitValidateResults.add(unitValidateResult);
            }
        });
        return unitValidateResults;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void doCommitAuth(String reportId, ReportFldStatus submit) {
        ReportJobInfo reportJobInfo = recordProcessDao.getReportJobInfoByReportId(reportId);
        if(submit==ReportFldStatus.SUBMIT){
            recordProcessDao.updateReportStatus(reportId,ReportStatus.SUBMIT.getValue());
        }else if(submit==ReportFldStatus.UNSUB){
            recordProcessDao.updateReportStatus(reportId,ReportStatus.UNSUB.getValue());
        }
        this.changeReportDataStatus(reportJobInfo.getJob_id().toString(),reportJobInfo.getReport_id().toString(),submit.getValueInteger());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeReportDataStatus(String jobId,String reportId,Integer status){
        recordProcessDao.updateReportDataStatus(jobId,reportId,status);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void changeJobDataStatus(String jobId,Integer status){
        recordProcessDao.updatJobDataStatus(jobId,status);
    }

    @Override
    public Integer getMaxColumId(String jobId, String reportId, String jobUnitId) {
        Integer maxColumId = recordProcessDao.getMaxColumId(jobId, reportId,jobUnitId);
        return maxColumId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeReportByJobId(String jobId) {
        List<ReportJobInfo> reportJobInfos = recordProcessDao.getReportJobInfosByJobId(jobId);
        if(reportJobInfos!=null&&reportJobInfos.size()>0){
            for (ReportJobInfo reportJobInfo : reportJobInfos) {
                recordProcessDao.changeRecordJobStatus(reportJobInfo.getReport_id(),ReportStatus.REPORT_DONE.getValueInteger());
            }
        }
        recordProcessDao.changeRecordJobConfigStatus(jobId, JobConfigStatus.FAIL.value());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logUserGroup(List<JobPersonGroupLog> jobPersonGroupLogs) {
        if(jobPersonGroupLogs!=null&&jobPersonGroupLogs.size()>0){
            recordProcessDao.delLogUserGroupHistory(jobPersonGroupLogs.get(0).getJob_id().toString());
            for (JobPersonGroupLog jobPersonGroupLog : jobPersonGroupLogs) {
                recordProcessDao.logUserGroup(jobPersonGroupLog);
            }
        }
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
                    }else if(FldDataTypes.POINT.compareTo(fldDataType)){//定位 经纬度 坐标
                    }else if(FldDataTypes.PICTURE.compareTo(fldDataType)){//图片
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadRecordFldFile(String reportId, String unitId, String columId, String fldId, MultipartFile uploadFile){
        try {
            ReportJobInfo reportJobInfo = recordProcessDao.getReportJobInfoByReportId(reportId);
            JobConfig jobConfig = recordProcessDao.getJobConfigByJobId(reportJobInfo.getJob_id().toString());
            String originName = reportJobInfo.getRecord_origin_name();
            String jobName = jobConfig.getJob_name();
            String uploadFileFullPath = this.getUploadFilePath(new StringBuilder().append(jobName).append("/").append(originName).toString());
            File upFilePath = new File(uploadFileFullPath);
            if(!upFilePath.exists()){
                upFilePath.mkdirs();
            }
            upFilePath.mkdirs();
            String uploadFileFullName = new StringBuilder().append(uploadFileFullPath).append("/").append(uploadFile.getOriginalFilename()).toString();
            File upFile = new File(uploadFileFullName);
            upFile.deleteOnExit();
            upFile.createNewFile();
            uploadFile.transferTo(upFile);
            ReportJobData reportJobData = new ReportJobData();
            reportJobData.setReport_id(new Integer(reportId));
            reportJobData.setUnit_id(new Integer(unitId));
            reportJobData.setColum_id(new Integer(columId));
            reportJobData.setFld_id(new Integer(fldId));
            reportJobData.setRecord_data(uploadFileFullName);
            recordProcessDao.updateReportJobData(reportJobData,new Integer(columId),jobConfig.getJob_id());
            return uploadFileFullName;
        } catch (IOException e) {
            e.printStackTrace();
            throw  new WorkbenchRuntimeException(e.getMessage(),e);
        }
    }

    @Override
    public File dowloadRecordFldFile(String reportId, String unitId, String columId, String fldId) {
        ReportJobInfo recordInfo = recordProcessDao.getReportJobInfoByReportId(reportId);
        ReportJobData reportData = recordProcessDao.getReportJobData(recordInfo.getJob_id().toString(), reportId, unitId, columId, fldId);
        String reportDataVal = reportData.getRecord_data();
        if(Strings.isNullOrEmpty(reportDataVal)){
            return null;
        }else{
            File recordFile = new File(reportDataVal);
            if(recordFile.exists()){
                return recordFile;
            }else
                return null;
        }
    }

    private String getUploadFilePath(String custPath){
        String uploadFilePath = upDownLoadFileConfig.getUploadFilePath();
        StringBuilder sb = new StringBuilder();
        sb.append(uploadFilePath).append("/").append(custPath);
        return sb.toString();
    }
}
