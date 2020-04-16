package com.datarecord.webapp.process.service.imp;

import com.datarecord.enums.JobConfigStatus;
import com.datarecord.enums.ReportFileLogStatus;
import com.datarecord.enums.ReportStatus;
import com.datarecord.webapp.fillinatask.bean.UpDownLoadFileConfig;
import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.dao.IRecordProcessFlowDao;
import com.datarecord.webapp.process.dao.ReportFileLog;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessFlowService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.utils.DataRecordUtil;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.auth.user.entity.UserType;
import com.workbench.exception.runtime.WorkbenchRuntimeException;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("recordProcessFlowService")
public class RecordProcessFlowServiceImp implements RecordProcessFlowService {

    private Logger logger = LoggerFactory.getLogger(RecordProcessFlowServiceImp.class);

    @Autowired
    private OriginService originService;

    @Autowired
    private IRecordProcessFlowDao recordProcessFlowDao;

    @Autowired
    protected IRecordProcessDao recordProcessDao;

    @Autowired
    private UpDownLoadFileConfig upDownLoadFileConfig;

    @Override
    public PageResult pageReportDatas(BigInteger user_id, String currPage, String pageSize, Map<String, String> queryParams) {
        Page<JobConfig> pageData = recordProcessFlowDao.pageReportDatas(currPage, pageSize, user_id, queryParams);

        PageResult result = PageResult.pageHelperList2PageResult(pageData);

        return result;
    }

    @Override
    public PageResult pageReviewDatas(String currPage, String pageSize, String jobId,String reportStatus) {
        Page<ReportJobInfo> pageData =  recordProcessFlowDao.pageReviewDatasByJob(currPage,pageSize,jobId,reportStatus,null);
        PageResult result = PageResult.pageHelperList2PageResult(pageData);
        return result;
    }

    @Override
    public void exportRecordData(ExportParams exportParams) {
        ReportFileLog reportFileLog = new ReportFileLog();
        reportFileLog.setReport_id(new Integer(exportParams.getReport_id()));
        reportFileLog.setJob_id(exportParams.getJobConfig().getJob_id());
        reportFileLog.setLog_status(ReportFileLogStatus.CREATING.getValue());
        reportFileLog.setStart_time(new Date());
//        reportFileLog.setLog_user(WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id());
        reportFileLog.setLog_user(new BigInteger("1"));
        recordProcessFlowDao.recordFileLog(reportFileLog);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<JobUnitConfig> exportUnits = exportParams.getJobConfig().getJobUnits();
                if(exportUnits!=null&&exportUnits.size()>0){
                    String reportId = exportParams.getReport_id();
                    Integer jobId = exportParams.getJobConfig().getJob_id();
                    JobConfig jobConfig = recordProcessDao.getJobConfigByJobId(jobId.toString());
                    List<JobUnitConfig> jobUnitConfigs = jobConfig.getJobUnits();
                    Map<Integer,JobUnitConfig> jobUnitConfigMapTmp = new HashMap<>();
                    for (JobUnitConfig jobUnitConfig : jobUnitConfigs) {
                        jobUnitConfigMapTmp.put(jobUnitConfig.getJob_unit_id(),jobUnitConfig);
                    }

                    ReportJobInfo reportDataInfo = recordProcessDao.getReportJobInfoByReportId(reportId);
                    HSSFWorkbook wb = new HSSFWorkbook();
                    for (JobUnitConfig exportUnit : exportUnits) {
                        Integer exportUnitId = exportUnit.getJob_unit_id();
                        if(exportUnitId==null){
                            logger.error("接收到空的组");
                            continue;
                        }
                        List<ReportJobData> reportDatas = recordProcessDao.getReportDataByUnitId(jobId.toString(), reportId, exportUnitId.toString());

                        List<ReportFldConfig> exportFlds = exportUnit.getUnitFlds();
                        if(exportFlds!=null&&exportFlds.size()>0){
                            List<Integer> exportFldsIds = new ArrayList<>();
                            for (ReportFldConfig exportFld : exportFlds) {
                                if(exportFld!=null&&exportFld.getFld_id()!=null){
                                    exportFldsIds.add(exportFld.getFld_id());
                                }else
                                    logger.error("有空的指标项");
                            }
                            if(!jobUnitConfigMapTmp.containsKey(exportUnit.getJob_unit_id())){
                                logger.error("未找到ID为:{}的报送组",exportUnit.getJob_unit_id());
                                continue;
                            }
                            JobUnitConfig exportUnitConfig = jobUnitConfigMapTmp.get(exportUnit.getJob_unit_id());

                            HSSFSheet unitSheet = wb.createSheet(exportUnitConfig.getJob_unit_name());
                            //按行 分组
                            Map<Integer,Map<Integer,String>> reportDatasMap = new LinkedHashMap<>();
                            for (ReportJobData reportData : reportDatas) {
                                //是否需要导出
                                if(exportFldsIds.contains(reportData.getFld_id())){
                                    Integer columId = reportData.getColum_id();
                                    if(!reportDatasMap.containsKey(columId)){
                                        reportDatasMap.put(columId,new HashMap<>());
                                    }
                                    reportDatasMap.get(columId).put(reportData.getFld_id(),reportData.getRecord_data());
                                }
                            }
                            //生成标题
                            List<ReportFldConfig> unitFldConfigs = exportUnitConfig.getUnitFlds();
                            Map<Integer,String> reportFldNames = new HashMap<>();
                            for (ReportFldConfig unitFldConfig : unitFldConfigs) {
                                StringBuilder fldNameSb = new StringBuilder();
                                fldNameSb.append(unitFldConfig.getFld_name());
                                if(!Strings.isNullOrEmpty(unitFldConfig.getFld_point())){
                                    fldNameSb.append("(");
                                    fldNameSb.append(unitFldConfig.getFld_point());
                                    fldNameSb.append(")");
                                }

                                reportFldNames.put(unitFldConfig.getFld_id(),fldNameSb.toString());
                            }
                            Integer dataRowIndex = 0;
                            Integer dataCellIndex = 0;
                            HSSFRow titleRow = unitSheet.createRow(dataRowIndex);
                            for (Integer exportFldsId : exportFldsIds) {
                                HSSFCell titleCell = titleRow.createCell(dataCellIndex);
                                titleCell.setCellValue(reportFldNames.get(exportFldsId));
                                logger.debug("写入行:{},列:{},值--{}",dataRowIndex,dataCellIndex,reportFldNames.get(exportFldsId));
                                dataCellIndex++;
                            }

                            dataRowIndex++;
                            dataCellIndex = 0;

                            for (Integer exportColumId : reportDatasMap.keySet()) {
                                HSSFRow rowTmp = unitSheet.createRow(dataRowIndex);
                                Map<Integer, String> exportFldDatas = reportDatasMap.get(exportColumId);
                                for (Integer exportFldsId : exportFldsIds) {
                                    String data = exportFldDatas.get(exportFldsId);
                                    HSSFCell dataCell = rowTmp.createCell(dataCellIndex);
                                    dataCell.setCellValue(data);
                                    logger.debug("写入行:{},列:{},值--{}",dataRowIndex,dataCellIndex,data);

                                    dataCellIndex++;
                                }

                                dataRowIndex++;
                                dataCellIndex = 0;
                            }
                        }
                    }

                    // /home/song/Downloads
                    FileOutputStream fout = null;
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                        String nowDate = dateFormat.format(new Date());

                        String filePath = new StringBuilder(upDownLoadFileConfig.getExportFilePath())
                                .append("/")
                                .append(jobConfig.getJob_name())
                                .append("/")
                                .toString();
                        File pathDic = new File(filePath);
                        if(!pathDic.exists()){
                            pathDic.mkdirs();
                        }

                        String fullFileName = new StringBuilder()
                                .append(filePath)
                                .append(jobConfig.getJob_name())
                                .append("-")
                                .append(reportDataInfo.getRecord_origin_name())
                                .append("-")
                                .append(nowDate)
                                .append(".xls")
                                .toString();
                        fout = new FileOutputStream(fullFileName);
                        wb.write(fout);

                        reportFileLog.setLog_status(ReportFileLogStatus.DONE.getValue());
                        reportFileLog.setEnd_time(new Date());
                        reportFileLog.setFile_path(fullFileName);
                        recordProcessFlowDao.updateRecordFileLog(reportFileLog);
                    } catch (Exception e) {
                        e.printStackTrace();
                        reportFileLog.setLog_status(ReportFileLogStatus.ERROR.getValue());
                        reportFileLog.setEnd_time(new Date());
                        reportFileLog.setComment(e.getMessage());
                        recordProcessFlowDao.updateRecordFileLog(reportFileLog);
                    }  finally {
                        try {
                            fout.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }else{//没有组
                    reportFileLog.setLog_status(ReportFileLogStatus.ERROR.getValue());
                    reportFileLog.setEnd_time(new Date());
                    reportFileLog.setComment("没有选择组数据");
                    recordProcessFlowDao.updateRecordFileLog(reportFileLog);
                }
            }
        },"CreateExportFile").start();



    }

    @Override
    public List<ReportFileLog> listReportFile(String reportId) {
        List<ReportFileLog> result = recordProcessFlowDao.listReportFile(reportId);
        return result;
    }

    @Override
    public ReportFileLog getReportFile(String logId) {
        ReportFileLog reportFileLog = recordProcessFlowDao.getReportFile(logId);
        return reportFileLog;
    }

    /**
     * 审批列表,查询当前用户发布的填报任务.
     * 如果当前用户为系统管理员user_type=3,则可查询全部数据
     * @param currPage
     * @param pageSize
     * @param reportStatus
     * @param reportName
     * @param reportOrigin
     * @return
     */
    @Override
    public PageResult pageJob(String currPage, String pageSize, String reportStatus, String reportName, String reportOrigin) {
        //查询当前用户是否有审批权限
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        BigInteger userId = user.getUser_id();
        if(DataRecordUtil.isSuperUser()){//当前用户是否是超级管理员
            userId = null;
        }
        //获取当前用户所属机构
//        Origin userOrigin = originService.getOriginByUser(user.getUser_id());

        //获取有权限机构下的所有已提交的报表
        Page<ReportJobInfo> resultDatas = recordProcessFlowDao.pageReviewDatas(
                new Integer(currPage), new Integer(pageSize), userId,
                Strings.emptyToNull(reportStatus),
                Strings.emptyToNull(reportName)
                );

        PageResult pageResult = PageResult.pageHelperList2PageResult(resultDatas);

        List<ReportJobInfo> dataList = pageResult.getDataList();
        Date currDate = new Date();

        for (ReportJobInfo reportCustomer : dataList) {
            Date startDate = reportCustomer.getJob_start_dt();
            Date endDate = reportCustomer.getJob_end_dt();

            if(ReportStatus.REPORT_DONE.compareTo(reportCustomer.getRecord_status())){
                continue;
            }

            if(currDate.compareTo(startDate)<0){//未到填报日期
                reportCustomer.setRecord_status(ReportStatus.TOO_EARLY.getValueInteger());
            }
            if(currDate.compareTo(endDate)>0){//已过期
                reportCustomer.setRecord_status(ReportStatus.OVER_TIME.getValueInteger());
                recordProcessDao.changeRecordJobStatus(reportCustomer.getReport_id(),ReportStatus.OVER_TIME.getValueInteger());
            }else{
//                if(ReportStatus.OVER_TIME.compareTo(reportCustomer.getRecord_status())){
//                    recordProcessDao.changeRecordJobStatus(reportCustomer.getReport_id(),ReportStatus.NORMAL.getValueInteger());
//                }
            }

        }

        return pageResult;
    }

    @Override
    public void reviewFld(String fldId,String reviewStatus) {
        recordProcessFlowDao.reviewFld(fldId,reviewStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewUnit(String unitId, String reviewStatus) {
        List<ReportFldConfig> flds = recordProcessDao.getFldByUnitId(unitId);
        for (ReportFldConfig fld : flds) {
            Integer fldId = fld.getFld_id();
            if(fld.getFld_status()==0){
                this.reviewFld(fldId.toString(),reviewStatus);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewJobItems( JobFlowLog jobFlowLog) {
        Integer jobId = jobFlowLog.getJob_id();
        JobConfig jobConfig = recordProcessDao.getJobConfigByJobId(jobId.toString());
//        String fldStatus = reviewStatus;
        Integer reviewStatusInt = new Integer(jobFlowLog.getJob_flow_status());
//        if(JobConfigStatus.APPROVE.compareWith(reviewStatusInt)){
//            fldStatus = FldConfigStatus.APPROVE.getValue();
//        }else if(JobConfigStatus.REJECT.compareWith(reviewStatusInt)){
//            fldStatus = FldConfigStatus.REJECT.getValue();
//        }

        List<JobUnitConfig> jobUnits = jobConfig.getJobUnits();
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        jobFlowLog.setJob_flow_user(user.getUser_id());
        jobFlowLog.setJob_flow_date(new Date());
        recordProcessFlowDao.changeJobConfig(jobId.toString(),jobFlowLog.getJob_flow_status().toString());
        recordProcessFlowDao.recordFlowLog(jobFlowLog);
//        for (JobUnitConfig jobUnit : jobUnits) {
//            this.reviewUnit(String.valueOf(jobUnit.getJob_unit_id()),fldStatus);
//        }
    }

    @Override
    public PageResult pageReviewJobConfigs(String user_id, String currPage, String pageSize, Map<String, String> queryParams) {
        List<Origin> childrenOrigins = null;

        Object fldOrigin = queryParams.get("jobOrigin");
        if(fldOrigin!=null){
            childrenOrigins = new ArrayList<>();
            Origin origin = new Origin();
            if(fldOrigin instanceof Double){
                Double fldOriginDb = (Double) fldOrigin;
                fldOriginDb.intValue();
                origin.setOrigin_id(new BigInteger(fldOriginDb.toString()));
            }else if(fldOrigin instanceof String){
                origin.setOrigin_id(new BigInteger(String.valueOf(fldOrigin)));
            }
            childrenOrigins.add(origin);
        }
        else{
//            User user = WorkbenchShiroUtils.checkUserFromShiroContext();
//            if(!UserType.SYSMANAGER.compareTo(user.getUser_type())){
//                childrenOrigins = this.checkAuthOrigins(user_id);
//            }
        }
        queryParams.put("jobName",Strings.emptyToNull(queryParams.get("jobName")));
        queryParams.put("jobStatus",Strings.emptyToNull(queryParams.get("jobStatus")));
        Page<JobConfig> reviewJobs =  recordProcessFlowDao.pageReviewJobs(currPage,pageSize,childrenOrigins,queryParams);
        PageResult pageResult = PageResult.pageHelperList2PageResult(reviewJobs);

        return pageResult;
    }

    @Override
    public PageResult pageReviewFlds(String user_id, String currPage, String pageSize, Map<String, String> queryParams) {
        List<Origin> childrenOrigins = new ArrayList<>();
        Object fldOrigin = queryParams.get("fldOrigin");
        if(fldOrigin!=null){
            Origin origin = new Origin();
            if(fldOrigin instanceof Double){
                Double fldOriginDb = (Double) fldOrigin;
                origin.setOrigin_id(new BigInteger(String.valueOf(fldOriginDb)));
            }else if(fldOrigin instanceof String){
                origin.setOrigin_id(new BigInteger(String.valueOf(fldOrigin)));
            }
            childrenOrigins.add(origin);
        }else{
            childrenOrigins = this.checkAuthOrigins(user_id);
        }
        queryParams.put("fldName",Strings.emptyToNull(queryParams.get("fldName")));
        queryParams.put("fldStatus",Strings.emptyToNull(queryParams.get("fldStatus")));
        Page<ReportFldConfig> reportFldConfigPage =  recordProcessFlowDao.pageReviewFlds(currPage,pageSize,childrenOrigins,queryParams);
        PageResult pageResult = PageResult.pageHelperList2PageResult(reportFldConfigPage);

        return pageResult;
    }

    @Override
    public void subJobConfig(String jobId) {
        recordProcessFlowDao.changeJobConfig(jobId,String.valueOf(JobConfigStatus.REVIEW.value()));
    }

    @Override
    public List<JobFlowLog> getJobFlowLogs(String jobId) {
        List<JobFlowLog> flowLogs = recordProcessFlowDao.listJobFlowLogs(jobId);
        return flowLogs;
    }

    private List<Origin> checkAuthOrigins(String user_id){
        Origin userOrigin = originService.getOriginByUser(new BigInteger(user_id));
        if(userOrigin==null){
            throw new WorkbenchRuntimeException("找不到当前用户对应的机构",new RuntimeException(""));
        }
        List<Origin> childrenOrigins = originService.checkAllChildren(userOrigin.getOrigin_id());
        childrenOrigins.add(0,userOrigin);
        return childrenOrigins;
    }
}

