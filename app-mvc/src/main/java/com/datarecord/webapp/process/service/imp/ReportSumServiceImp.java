package com.datarecord.webapp.process.service.imp;

import com.datarecord.enums.FldDataTypes;
import com.datarecord.enums.ReportFileLogStatus;
import com.datarecord.enums.ReportFldStatus;
import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.fillinatask.bean.UpDownLoadFileConfig;
import com.datarecord.webapp.process.RecordFileService;
import com.datarecord.webapp.process.dao.IRecordFileDao;
import com.datarecord.webapp.process.dao.IReportSumDao;
import com.datarecord.webapp.process.dao.ReportFileLog;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.datarecord.webapp.process.service.ReportSumService;
import com.datarecord.webapp.utils.DataRecordUtil;
import com.google.common.base.Strings;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("reportSumService")
public class ReportSumServiceImp implements ReportSumService {

    private Logger logger = LoggerFactory.getLogger(ReportSumServiceImp.class);

    @Autowired
    private IReportSumDao reportSumDao;

    @Autowired
    private IRecordFileDao recordFileDao;

    @Autowired
    private RecordProcessService recordProcessService;

    @Autowired
    private UpDownLoadFileConfig upDownLoadFileConfig;

    @Autowired
    private RecordFileService recordFileService;

    private final Integer FILE_REPORT_EACH_GROUP = 0;
    private final Integer FILE_REPORT_SELECT_FLDS = 1;
    private final Integer FILE_JOB_ALL_REPORT = 2;
    private final Integer FILE_USER_UPLOAD = 3;
    private final Integer FILE_JOB_SELECT_FLDS = 4;
    private final Integer FILE_REPORT_SINGLE = 5;

    @Override
    public Map<Integer,List<ReportJobData>> recordDataByFlds(ExportParams exportParams){
        JobConfig jobConfigParams = exportParams.getJobConfig();
        Integer jobId = jobConfigParams.getJob_id();
        String reportId = exportParams.getReport_id();
        List<JobUnitConfig> groups = jobConfigParams.getJobUnits();
        Map<Integer,List<ReportJobData>> groupDataMap = new HashMap<>();
        Boolean isSuperUser = DataRecordUtil.isSuperUser();

        if(groups!=null){
            for (JobUnitConfig group : groups) {
                Integer groupId = group.getJob_unit_id();
                List<ReportFldConfig> groupFlds = group.getUnitFlds();
                List<Integer> fldsTmp = new ArrayList<>();
                List<ReportJobData> reportDataResult = new ArrayList<>();
                for (ReportFldConfig fldConfig : groupFlds) {
                    fldsTmp.add(fldConfig.getFld_id());
                }
//                List<ReportJobData> reportDatas = recordProcessService.getFldReportDatas(jobId.toString(), reportId, groupId.toString());
                List<ReportJobData> reportDatas = recordProcessService.getUnitDatas(jobId.toString(), Strings.emptyToNull(reportId), groupId.toString());
                if(reportDatas!=null){
                    for (ReportJobData reportData : reportDatas) {
                        if(!isSuperUser){
                            if(reportData.getData_status()==ReportFldStatus.NORMAL.getValueInteger()){
                                continue;
                            }
                        }

                        if(fldsTmp.contains(reportData.getFld_id())){
                            reportDataResult.add(reportData);
                        }
                    }
                }
                groupDataMap.put(groupId,reportDataResult);
            }
        }
        if(exportParams.getNeedExport()!=null&&exportParams.getNeedExport()){
            if(Strings.isNullOrEmpty(exportParams.getReport_id())){
                this.exportJobFldsData(exportParams);
            }else{
                this.exportRecordFldsData(exportParams);
            }
        }

        return groupDataMap;
    }


    @Override
    public ReportFileLog getReportFile(String logId) {
        ReportFileLog reportFileLog = recordFileDao.getReportFile(logId);
        return reportFileLog;
    }

    @Override
    public List<ReportFileLog> getSumJobFileList(String jobId){
        return getJobFileList(jobId,2);
    }

    @Override
    public List<ReportFileLog> getSumJobFldFileList(String jobId){
        return getJobFileList(jobId,4);
    }

    private List<ReportFileLog> getJobFileList(String jobId,Integer logType){
        BigInteger userId = WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id();
        List<ReportFileLog> sumFileList = reportSumDao.getSumJobFileList(jobId,userId,logType);
        return sumFileList;
    }

    @Override
    public void sumJobFiles(String jobId) {
        BigInteger userId = WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id();
//        BigInteger userId = new BigInteger("1");
        ReportFileLog jobFileLog = recordFileService.recordFileCreateLog(null, jobId, userId, FILE_JOB_ALL_REPORT);

        new Thread(() -> {
            String fileFullPath = null;
            FileOutputStream fout = null;
            try{
                JobConfig jobConfig = recordProcessService.getJobConfigByJobId(jobId);
                List<ReportJobInfo> jobInfos = recordProcessService.getReportJobInfosByJobId(jobId);
                List<JobUnitConfig> allUnits = jobConfig.getJobUnits();
                HSSFWorkbook wb = new HSSFWorkbook();
                if(allUnits!=null&&allUnits.size()>0){
                    for (JobUnitConfig unitConfig : allUnits) {
                        Integer jobUnitId = unitConfig.getJob_unit_id();
                        HSSFSheet unitSheet = wb.createSheet(unitConfig.getJob_unit_name());
                        List<ReportFldConfig> exportUnitFldConfigs = unitConfig.getUnitFlds();

                        writeExcelTitle(exportUnitFldConfigs,unitSheet);
                        Integer rowIndex = 1;
                        for (ReportJobInfo jobInfo : jobInfos) {
                            List<ReportJobData> reportDatas = recordProcessService.getFldReportDatas(jobId, jobInfo.getReport_id().toString(), jobUnitId.toString());
                            //按行分组数据
                            Map<Integer, Map<Integer, String>> reportDatasMap = groupRowDatas(reportDatas);
                            //获取数据字典的字典数据
                            Map<Integer, Map<String, String>> fldDictMap = getFldDicts(exportUnitFldConfigs);
                            //生成内容
                            writeExcelValues(reportDatasMap,exportUnitFldConfigs,fldDictMap ,unitSheet,rowIndex);

                            rowIndex = rowIndex+reportDatasMap.size();
                        }

                    }
                }


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String nowDate = dateFormat.format(new Date());
                String filePath = createFilePath(new StringBuilder().append("汇总").append("/").append(jobConfig.getJob_name()).toString());

                fileFullPath = new StringBuilder()
                        .append(filePath)
                        .append(jobConfig.getJob_name())
                        .append("-")
                        .append(nowDate)
                        .append(".xls")
                        .toString();
                fout = new FileOutputStream(fileFullPath);
                wb.write(fout);
            }catch (Exception e){
                recordFileService.updateFileLogStatus(jobFileLog.getLog_id(),ReportFileLogStatus.ERROR,null,e.getMessage());
            }finally {
                if(fout!=null) {
                    try {
                        fout.close();
                    } catch (IOException e) {
                        recordFileService.updateFileLogStatus(jobFileLog.getLog_id(),ReportFileLogStatus.ERROR,fileFullPath,e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
            recordFileService.updateFileLogStatus(jobFileLog.getLog_id(),ReportFileLogStatus.DONE,fileFullPath,null);
        },"SumJobFiles").start();



    }

    @Override
    public String exportSigleReportData(String reportId){
        JobConfig jobConfig = recordProcessService.getJobConfigByReportId(reportId);
        ReportFileLog reportFileLog = recordFileService.recordFileCreateLog(
                reportId, jobConfig.getJob_id().toString(),
                WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id(), FILE_REPORT_SINGLE);
        ExportParams exportParams = new ExportParams();
        exportParams.setNeedExport(true);
        exportParams.setReport_id(reportId);
        exportParams.setJobConfig(jobConfig);
        return this.doCreateFile(exportParams,reportFileLog);
    }

    @Override
    public void exportJobFldsData(ExportParams exportParams){
        ReportFileLog reportFileLog = recordFileService.recordFileCreateLog(
                exportParams.getReport_id(), exportParams.getJobConfig().getJob_id().toString(),
                WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id(), FILE_JOB_SELECT_FLDS);

        this.threadRunner(exportParams,reportFileLog);
    }

    @Override
    public void exportRecordFldsData(ExportParams exportParams) {
        ReportFileLog reportFileLog = recordFileService.recordFileCreateLog(
                exportParams.getReport_id(), exportParams.getJobConfig().getJob_id().toString(),
                WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id(), FILE_REPORT_SELECT_FLDS);

        this.threadRunner(exportParams,reportFileLog);
    }

    private void threadRunner(ExportParams exportParams, ReportFileLog reportFileLog){
        new Thread(new Runnable() {
            @Override
            public void run() {
                doCreateFile(exportParams,reportFileLog);
            }
        },"CreateExportFile").start();

    }

    private String doCreateFile(ExportParams exportParams, ReportFileLog reportFileLog){
        List<JobUnitConfig> exportUnits = exportParams.getJobConfig().getJobUnits();
        if(exportUnits!=null&&exportUnits.size()>0){
            String reportId = exportParams.getReport_id();
            Integer jobId = exportParams.getJobConfig().getJob_id();
            JobConfig jobConfig = recordProcessService.getJobConfigByJobId(jobId.toString());
            List<JobUnitConfig> jobUnitConfigs = jobConfig.getJobUnits();
            Map<Integer,JobUnitConfig> jobUnitConfigMapTmp = new HashMap<>();
            for (JobUnitConfig jobUnitConfig : jobUnitConfigs) {
                jobUnitConfigMapTmp.put(jobUnitConfig.getJob_unit_id(),jobUnitConfig);
            }

            ReportJobInfo reportDataInfo = Strings.isNullOrEmpty(reportId)?null:recordProcessService.getReportJobInfo(reportId);
            HSSFWorkbook wb = new HSSFWorkbook();
            for (JobUnitConfig exportUnit : exportUnits) {
                Integer exportUnitId = exportUnit.getJob_unit_id();
                if(exportUnitId==null){
                    logger.error("接收到空的组");
                    continue;
                }

//                        List<ReportJobData> reportDatas = recordProcessService.getFldReportDatas(jobId.toString(), reportId, exportUnitId.toString());
                List<ReportJobData> reportDatas = recordProcessService.getUnitDatas(jobId.toString(),Strings.emptyToNull(reportId),exportUnitId.toString());
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

                    List<ReportFldConfig> unitFldConfigs = exportUnitConfig.getUnitFlds();
                    List<ReportFldConfig> exportUnitFldConfigs = new ArrayList<>();

                    for (ReportFldConfig unitFldConfig : unitFldConfigs) {
                        if(exportFldsIds.contains(unitFldConfig.getFld_id())){
                            exportUnitFldConfigs.add(unitFldConfig);
                        }
                    }

                    //生成标题
                    writeExcelTitle(exportUnitFldConfigs,unitSheet);
                    //按行分组数据
//                            Map<Integer, Map<Integer, String>> reportDatasMap = groupRowDatas(reportDatas);
                    Map<Integer, Map<Integer, Map<Integer, String>>> reportDatasMap = groupReportRowsDatas(reportDatas);
                    //获取数据字典的字典数据
                    Map<Integer, Map<String, String>> fldDictMap = getFldDicts(exportUnitFldConfigs);
                    //生成内容
                    Integer rowIndex = 1;
                    for (Integer exportReportId : reportDatasMap.keySet()) {
                        Map<Integer, Map<Integer, String>> reportRowDatas = reportDatasMap.get(exportReportId);
                        writeExcelValues(reportRowDatas, exportUnitFldConfigs, fldDictMap, unitSheet, rowIndex);
                        rowIndex = rowIndex + reportRowDatas.size();
                    }

                }
            }

            FileOutputStream fout = null;
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String nowDate = dateFormat.format(new Date());
                String filePath = createFilePath(jobConfig.getJob_name());

                StringBuilder fullFileNamesb = new StringBuilder()
                        .append(filePath)
                        .append(jobConfig.getJob_name())
                        .append("-");
                if(reportDataInfo!=null){
                    fullFileNamesb.append(reportDataInfo.getRecord_origin_name())
                            .append("-");
                }

                fullFileNamesb.append(nowDate)
                        .append(".xls")
                        .toString();
                fout = new FileOutputStream(fullFileNamesb.toString());
                wb.write(fout);
                recordFileService.updateFileLogStatus(reportFileLog.getLog_id(),ReportFileLogStatus.DONE,fullFileNamesb.toString(),null);
                return fullFileNamesb.toString();
            } catch (Exception e) {
                e.printStackTrace();
                recordFileService.updateFileLogStatus(reportFileLog.getLog_id(),ReportFileLogStatus.ERROR,null,e.getMessage());

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
            recordFileDao.updateRecordFileLog(reportFileLog);
        }

        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String exportGroup(String reportId, String jobId,String groupId) {
        ReportFileLog fileLog = recordFileService.recordFileCreateLog(reportId, jobId,
                WorkbenchShiroUtils.checkUserFromShiroContext().getUser_id(), FILE_REPORT_EACH_GROUP);
//        ReportFileLog fileLog = this.recordFileCreateLog(reportId, jobId, new BigInteger("1"), 0);

        JobConfig jobConfig = recordProcessService.getJobConfigByJobId(jobId);

        ReportJobInfo reportDataInfo = recordProcessService.getReportJobInfo(reportId);

        return this.exportGroup(reportDataInfo,jobConfig,groupId,fileLog);

    }

    private String exportGroup(ReportJobInfo reportDataInfo, JobConfig jobConfig,String groupId,ReportFileLog fileLog){
        List<JobUnitConfig> allUnits = jobConfig.getJobUnits();
        String jobId = jobConfig.getJob_id().toString();
        String reportId = reportDataInfo.getReport_id().toString();
        HSSFWorkbook wb = new HSSFWorkbook();
        for (JobUnitConfig unitConfig : allUnits) {
            if(unitConfig.getJob_unit_id().toString().equals(groupId)){
                HSSFSheet unitSheet = wb.createSheet(unitConfig.getJob_unit_name());
                List<ReportFldConfig> unitFlds = unitConfig.getUnitFlds();
                List<ReportJobData> reportDatas = recordProcessService.getFldReportDatas(jobId, reportId, groupId);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
                String nowDate = dateFormat.format(new Date());

                String filePath = this.createFilePath(
                        new StringBuilder().append("/groupExport/").append(jobConfig.getJob_name()).append("/").append(unitConfig.getJob_unit_name()).toString());
                String fullFileName = new StringBuilder()
                        .append(filePath)
                        .append(jobConfig.getJob_name())
                        .append("-")
                        .append(unitConfig.getJob_unit_name())
                        .append("-")
                        .append(reportDataInfo.getRecord_origin_name())
                        .append("-")
                        .append(nowDate)
                        .append(".xls")
                        .toString();
                writeExcelTitle(unitFlds,unitSheet);
                Map<Integer, Map<Integer, String>> rowDatas = groupRowDatas(reportDatas);
                Map<Integer, Map<String, String>> fldDicts = getFldDicts(unitFlds);

                writeExcelValues(rowDatas,unitFlds,fldDicts,unitSheet,null);

                FileOutputStream fout = null;
                try {
                    fout = new FileOutputStream(fullFileName);
                    wb.write(fout);
                    recordFileService.updateFileLogStatus(fileLog.getLog_id(),ReportFileLogStatus.DONE,fullFileName,null);

                } catch (Exception e) {
                    e.printStackTrace();
                    recordFileService.updateFileLogStatus(fileLog.getLog_id(),ReportFileLogStatus.ERROR,fullFileName,e.getMessage());
                }  finally {
                    try {
                        fout.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return fullFileName;
            }
        }

        return null;
    }

    private void writeExcelTitle(List<ReportFldConfig> unitFldConfigs,HSSFSheet unitSheet){
        Map<Integer, Map<String,String>> fldDicts = new HashMap<>();
        HSSFRow titleRow = unitSheet.createRow(0);
        Integer cellCount = 0;
        for (ReportFldConfig unitFldConfig : unitFldConfigs) {
            StringBuilder fldNameSb = new StringBuilder();
            fldNameSb.append(unitFldConfig.getFld_name());
            if(!Strings.isNullOrEmpty(unitFldConfig.getFld_point())){
                fldNameSb.append("(");
                fldNameSb.append(unitFldConfig.getFld_point());
                fldNameSb.append(")");
            }
            HSSFCell titleCell = titleRow.createCell(cellCount);
            titleCell.setCellValue(fldNameSb.toString());
            cellCount++;
            logger.debug("写入行:{},列:{},值--{}",0,cellCount,fldNameSb.toString());

        }
    }

    //ReportId:ColumId-->ReportDataValue
    private Map<Integer,Map<Integer, Map<Integer, String>>> groupReportRowsDatas(List<ReportJobData> reportDatas){
        Map<Integer,Map<Integer, Map<Integer, String>>>  reportDatasMap = new LinkedHashMap<>();
        for (ReportJobData reportData : reportDatas) {
            if(!DataRecordUtil.isSuperUser()){
                if(reportData.getData_status()== ReportFldStatus.NORMAL.getValueInteger()){
                    continue;
                }
            }

            Integer reportId = reportData.getReport_id();
            Integer columId = reportData.getColum_id();
            if(!reportDatasMap.containsKey(reportId)){
                reportDatasMap.put(reportId,new LinkedHashMap<>());
            }
            if(!reportDatasMap.get(reportId).containsKey(columId)){
                reportDatasMap.get(reportId).put(columId,new LinkedHashMap<>());
            }
            reportDatasMap.get(reportId).get(columId).put(reportData.getFld_id(),reportData.getRecord_data());
        }
        return reportDatasMap;
    }

    private Map<Integer, Map<Integer, String>> groupRowDatas(List<ReportJobData> reportDatas){
        Map<Integer,Map<Integer,String>>  reportDatasMap = new LinkedHashMap<>();
        for (ReportJobData reportData : reportDatas) {
            if(!DataRecordUtil.isSuperUser()){
                if(reportData.getData_status()== ReportFldStatus.NORMAL.getValueInteger()){
                    continue;
                }
            }


            Integer columId = reportData.getColum_id();
            if(!reportDatasMap.containsKey(columId)){
                reportDatasMap.put(columId,new HashMap<>());
            }
            reportDatasMap.get(columId).put(reportData.getFld_id(),reportData.getRecord_data());
        }
        return reportDatasMap;
    }

    private Map<Integer, Map<String, String>> getFldDicts(List<ReportFldConfig> exportUnitFldConfigs){
        Map<Integer,Map<String,String>> fldDicts = new HashMap<>();
        for (ReportFldConfig exportUnitFldConfig : exportUnitFldConfigs) {
            String fldDataType = exportUnitFldConfig.getFld_data_type();
            if(FldDataTypes.DICT.compareTo(fldDataType)){
                List<DataDictionary> dictList = recordProcessService.getDictcontent4Fld(exportUnitFldConfig.getFld_id());
                Map<String,String> dictMapTmp = new HashMap<>();
                if(dictList!=null&&dictList.size()>0){
                    for (DataDictionary dataDictionary : dictList) {
                        String val = dataDictionary.getDict_content_value();
                        String name = dataDictionary.getDict_content_name();
                        dictMapTmp.put(val,name);
                    }
                }
                fldDicts.put(exportUnitFldConfig.getFld_id(),dictMapTmp);
            }
        }
        return fldDicts;
    }

    private void writeExcelValues(
            Map<Integer, Map<Integer, String>> reportDatasMap,
            List<ReportFldConfig> exportUnitFldConfigs,
            Map<Integer, Map<String, String>> fldDicts,
            HSSFSheet unitSheet,
            Integer dataRowIndex) {
        if(dataRowIndex==null)
            dataRowIndex = 1;
        Integer dataCellIndex = 0;
        for (Integer exportColumId : reportDatasMap.keySet()) {
            HSSFRow rowTmp = unitSheet.createRow(dataRowIndex);
            Map<Integer, String> exportFldDatas = reportDatasMap.get(exportColumId);
            for (ReportFldConfig exportUnitFldConfig : exportUnitFldConfigs) {
                Integer exportFldsId = exportUnitFldConfig.getFld_id();
                String data = exportFldDatas.get(exportFldsId);
                //是否为字典
                if(fldDicts.containsKey(exportFldsId)){
                    data = fldDicts.get(exportFldsId).get(data);
                }
                HSSFCell dataCell = rowTmp.createCell(dataCellIndex);
                dataCell.setCellValue(data);
                logger.debug("写入行:{},列:{},值--{}",dataRowIndex,dataCellIndex,data);

                dataCellIndex++;
            }

            dataRowIndex++;
            dataCellIndex = 0;
        }
    }

    private String createFilePath(String expendPath){
        String filePath = new StringBuilder(upDownLoadFileConfig.getExportFilePath())
                .append("/")
                .append(expendPath)
                .append("/")
                .toString();
        File pathDic = new File(filePath);
        if(!pathDic.exists()){
            pathDic.mkdirs();
        }
        return filePath;
    }
}
