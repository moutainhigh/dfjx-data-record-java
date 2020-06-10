package com.datarecord.webapp.process.service.imp;

import com.datarecord.enums.JobUnitType;
import com.datarecord.webapp.fillinatask.bean.UpDownLoadFileConfig;
import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.dao.IRecordProcessImportDao;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessFilesService;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.google.common.base.Strings;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("recordProcessFilesService")
public class RecordProcessFilesServiceImp implements RecordProcessFilesService {

    private Logger logger = LoggerFactory.getLogger(RecordProcessFilesServiceImp.class);

    @Autowired
    private RecordProcessService recordProcessService;

    @Autowired
    private UpDownLoadFileConfig upDownLoadFileConfig;

    @Autowired
    private IRecordProcessImportDao recordProcessImportDao;

    @Autowired
    private RecordProcessService gridRecordProcessService;

    @Autowired
    private RecordProcessService simpleRecordProcessService;

    @Autowired
    private IRecordProcessDao recordProcessDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String getImportTemplate(String jobId) throws IOException {
        //检查是否已经存在生成过的模板
        RecordImportJobInfo recordImportJobInfo = recordProcessImportDao.getRecordImportJobInfo(jobId);
        String templateFilePath = null;
        if(recordImportJobInfo!=null){
            templateFilePath = recordImportJobInfo.getTemplate_file_path();
            if(new File(templateFilePath).exists())
                return templateFilePath;
            else
                recordProcessImportDao.deleteRecordImportJobInfo(jobId);
        }

        JobConfig jobConfig = recordProcessService.getJobConfigByJobId(jobId);

        String filePath = new StringBuilder(upDownLoadFileConfig.getUploadFilePath()).append("/").append("ImportTemplate")
                .append("/").append(jobConfig.getJob_name()).append("/").toString();
        File pathDic = new File(filePath);
        if(!pathDic.exists()){
            pathDic.mkdirs();
        }
        templateFilePath = new StringBuilder().append(filePath).append(jobConfig.getJob_id()).append(".xls").toString();
        File templateFile = new File(templateFilePath);
        if(!templateFile.exists()){
            templateFile.createNewFile();
        }
        recordImportJobInfo = new RecordImportJobInfo();
        recordImportJobInfo.setJob_id(new Integer(jobId));
        recordImportJobInfo.setTemplate_create_date(new Date());
        recordImportJobInfo.setTemplate_file_path(templateFilePath);
        recordProcessImportDao.recordJobTemplateInfo(recordImportJobInfo);

        Map<String, Map<String,List<String>>> groupMapTmp = new LinkedHashMap<>();
        Map<String, JobUnitConfig> groupConfigTmp = new HashMap<>();
        Map<String, ReportFldConfig> fldConfigTmp = new HashMap<>();

        List<JobUnitConfig> jobUnits = jobConfig.getJobUnits();
        for (JobUnitConfig jobUnit : jobUnits) {

            groupMapTmp.put(jobUnit.getJob_unit_name(),new LinkedHashMap<>());
            groupConfigTmp.put(jobUnit.getJob_unit_name(),jobUnit);
            List<ReportFldConfig> unitFlds = jobUnit.getUnitFlds();
            for (ReportFldConfig unitFld : unitFlds) {
                String catgName = unitFld.getCatg_name();
                String fldName = unitFld.getFld_name();
                if(!groupMapTmp.get(jobUnit.getJob_unit_name()).containsKey(catgName)){
                    groupMapTmp.get(jobUnit.getJob_unit_name()).put(catgName,new LinkedList<>());
                }
                groupMapTmp.get(jobUnit.getJob_unit_name()).get(catgName).add(fldName);
                fldConfigTmp.put(unitFld.getFld_name(),unitFld);
            }
        }
        HSSFWorkbook wb = new HSSFWorkbook();

        if(groupMapTmp!=null&&groupMapTmp.size()>0){
            Integer unitOrder = 0;
            for (String unitName : groupMapTmp.keySet()) {
                JobUnitConfig jobUnitConfig = groupConfigTmp.get(unitName);
                RecordImportUnitInfo recordImportUnitInfo = new RecordImportUnitInfo();
                recordImportUnitInfo.setJob_id(new Integer(jobId));
                recordImportUnitInfo.setUnit_id(jobUnitConfig.getJob_unit_id());
                recordImportUnitInfo.setUnit_order(unitOrder);
                recordProcessImportDao.recordUnitTemplateInfo(recordImportUnitInfo);

                Map<String, List<String>> unitFldCatge = groupMapTmp.get(unitName);
                if(jobUnitConfig.getJob_unit_type().equals(JobUnitType.GRID.value())){
                    this.createGridSheet(jobUnitConfig,fldConfigTmp,unitFldCatge,wb);
                }else  if(groupConfigTmp.get(unitName).getJob_unit_type().equals(JobUnitType.SIMPLE.value())){
                    this.createSimpleSheet(jobUnitConfig,fldConfigTmp,unitFldCatge,wb);
                }
                unitOrder++;
            }
        }



        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(templateFilePath);
            wb.write(fout);
        } catch (IOException e) {
            throw  e;
        } finally {
            try {
                fout.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        logger.debug("group result {}",groupMapTmp);

        return templateFilePath;
    }

    @Override
    public void importRecordData(String jobId, String reportId, File importFile) throws IOException {
        RecordImportJobInfo recordImportJobInfo = recordProcessImportDao.getRecordImportJobInfo(jobId);
        List<RecordImportUnitInfo> recordImportUnitInfos = recordProcessImportDao.getRecordImportUnitInfos(jobId);
        recordImportJobInfo.setRecordImportUnitInfoList(recordImportUnitInfos);
        if(recordImportUnitInfos!=null&&recordImportUnitInfos.size()>0){
            for (RecordImportUnitInfo recordImportUnitInfo : recordImportUnitInfos) {
                List<RecordImportFldInfo> reportImportFldInfos =
                        recordProcessImportDao.getRecordImportFLdInfos(recordImportUnitInfo.getJob_id().toString(), recordImportUnitInfo.getUnit_id().toString());
                recordImportUnitInfo.setRecordImportFldInfoList(reportImportFldInfos);
            }
        }
        JobConfig jobConfig = recordProcessService.getJobConfigByJobId(jobId);
        List<JobUnitConfig> jobUnits = jobConfig.getJobUnits();
        Map<Integer,JobUnitConfig> jobUnitMap = new HashMap<>();
        for (JobUnitConfig jobUnit : jobUnits) {
            jobUnitMap.put(jobUnit.getJob_unit_id(),jobUnit);
        }

        FileInputStream fi = new FileInputStream(importFile);
        HSSFWorkbook excelWorkBook = new HSSFWorkbook(fi);
        Iterator<Sheet> allSheet = excelWorkBook.sheetIterator();
        int sheetUnitIndex = 0;
        while (allSheet.hasNext()){
            RecordImportUnitInfo unitTemplateInfo = recordImportJobInfo.getRecordImportUnitInfoList().get(sheetUnitIndex);
            unitTemplateInfo.getUnit_id();
            if(jobUnitMap.containsKey(unitTemplateInfo.getUnit_id())){
                JobUnitConfig jobUnit = jobUnitMap.get(unitTemplateInfo.getUnit_id());
                Sheet excelSheet = allSheet.next();
                if(JobUnitType.GRID.value() == jobUnit.getJob_unit_type()){
                    this.saveGridDatas(jobId,reportId,excelSheet,unitTemplateInfo);
                }else if(JobUnitType.SIMPLE.value() == jobUnit.getJob_unit_type()){
                    this.saveSimpleDatas(jobId,reportId,excelSheet,unitTemplateInfo);
                }
            }
            sheetUnitIndex++;
        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void overrideImport(String jobId, String reportId, File importFile) throws IOException {
        recordProcessDao.deleteRecordDataByReport(new Integer(jobId),new Integer(reportId));
        this.importRecordData(jobId,reportId,importFile);
    }

    @Override
    public void extendImport(String jobId, String reportId, File importFile) throws IOException {
        this.importRecordData(jobId,reportId,importFile);
    }

    void createGridSheet(JobUnitConfig jobUnitConfig, Map<String, ReportFldConfig> fldConfigTmp, Map<String, List<String>> catgMap, HSSFWorkbook wb){
        if(catgMap!=null&&catgMap.size()>0){
            HSSFSheet unitSheet = wb.createSheet(jobUnitConfig.getJob_unit_name());
            Integer catgNameOffset = 0;
            Integer catgNameIndex = 0;
            for (String catgName : catgMap.keySet()) {
                List<String> fldNameList = catgMap.get(catgName);
                if(fldNameList.size()<=0)
                    continue;
                HSSFRow rowObj = unitSheet.getRow(0);
                if(rowObj==null){
                    rowObj = unitSheet.createRow(0);
                }
                catgNameOffset = fldNameList.size();
                HSSFCell catgNameCell = rowObj.createCell(catgNameIndex);
                catgNameCell.setCellValue(catgName);
                logger.debug("Merge :{}.{}.{}",catgName,catgNameIndex,catgNameOffset);
                if(catgNameOffset>1){
                    CellRangeAddress region = new CellRangeAddress(0, 0, catgNameIndex, catgNameIndex+catgNameOffset-1);
                    unitSheet.addMergedRegion(region);
                }
                HSSFRow fldRowObj = unitSheet.getRow(1);
                if(fldRowObj==null){
                    fldRowObj = unitSheet.createRow(1);
                }
                for (String fldName : fldNameList) {
                    ReportFldConfig fldCOnfig = fldConfigTmp.get(fldName);
                    this.recordTemplateFldLog(jobUnitConfig,fldCOnfig,catgNameIndex);
                    HSSFCell fldNameCell = fldRowObj.createCell(catgNameIndex);
                    fldNameCell.setCellValue(fldName);
                    catgNameIndex++;
                }
            }

        }
    }

    public void createSimpleSheet(JobUnitConfig jobUnitConfig, Map<String, ReportFldConfig> fldConfigTmp, Map<String, List<String>> catgMap, HSSFWorkbook wb) {
        if(catgMap!=null&&catgMap.size()>0) {
            HSSFSheet unitSheet = wb.createSheet(jobUnitConfig.getJob_unit_name());
            for (String catgName : catgMap.keySet()) {
                List<String> fldNameList = catgMap.get(catgName);
                if(fldNameList.size()<=0)
                    continue;
                Integer fldRowIndex = 0;
                for (String fldName : fldNameList) {
                    HSSFRow excelRow = unitSheet.createRow(fldRowIndex);
                    HSSFCell fldNameCell = excelRow.createCell(0);
                    fldNameCell.setCellValue(fldName);
                    this.recordTemplateFldLog(jobUnitConfig,fldConfigTmp.get(fldName),fldRowIndex);
                    fldRowIndex++;
                }
            }
        }
    }

    public void recordTemplateFldLog(JobUnitConfig jobUnitConfig,ReportFldConfig fldCOnfig,Integer fldOrder){
        RecordImportFldInfo recordImportFldInfo = new RecordImportFldInfo();
        recordImportFldInfo.setJob_id(jobUnitConfig.getJob_id());
        recordImportFldInfo.setUnit_id(jobUnitConfig.getJob_unit_id());
        recordImportFldInfo.setFld_id(fldCOnfig.getFld_id());
        recordImportFldInfo.setFld_order(fldOrder);
        recordProcessImportDao.recordFldTemplateInfo(recordImportFldInfo);
    }


    private void saveSimpleDatas(String jobId, String reportId, Sheet excelSheet, RecordImportUnitInfo unitTemplateInfo) {
        List<ReportJobData> allDataList = new ArrayList<>();
        List<RecordImportFldInfo> recordImportFlds = unitTemplateInfo.getRecordImportFldInfoList();
        Integer rowIndex = 0;
        for (RecordImportFldInfo recordImportFldInfo : recordImportFlds) {
            Cell excelRowCell = excelSheet.getRow(rowIndex).getCell(1);

            String cellValue = this.getCellValue(excelRowCell);
            ReportJobData reportJobData = new ReportJobData();
            reportJobData.setRecord_data(Strings.nullToEmpty(cellValue));
            reportJobData.setColum_id(0);
            reportJobData.setFld_id(recordImportFldInfo.getFld_id());
            reportJobData.setUnit_id(unitTemplateInfo.getUnit_id());
            reportJobData.setReport_id(new Integer(reportId));
            allDataList.add(reportJobData);
        }
        SaveReportJobInfos saveReportJobInfos= new SaveReportJobInfos();
        saveReportJobInfos.setJob_id(new Integer(jobId));
        saveReportJobInfos.setReport_id(new Integer(reportId));
        saveReportJobInfos.setReportJobInfos(allDataList);
        simpleRecordProcessService.saveDatas(saveReportJobInfos);
    }

    private void saveGridDatas(String jobId, String reportId,Sheet excelSheet ,RecordImportUnitInfo unitTemplateInfo){
//        List<ReportJobData> importDataList = new ArrayList<>();
        Iterator<Row> allRows = excelSheet.rowIterator();
        List<Integer> oldColums = recordProcessService.getUnitColums(jobId, reportId, unitTemplateInfo.getUnit_id().toString());
        if(allRows!=null){
            Integer columIdIndex = 0;
            if(oldColums!=null&&oldColums.size()>0)
                columIdIndex = oldColums.size();
            List<RecordImportFldInfo> fldTemplateInfo = unitTemplateInfo.getRecordImportFldInfoList();
            int excelRowIndex = 0;
            while(allRows.hasNext()){
                Row excelRow = allRows.next();
                if(excelRowIndex<2){
                    excelRowIndex++;
                    continue;
                }
                Iterator<Cell> allRowCells = excelRow.cellIterator();
                if(allRowCells!=null){
                    Integer rowCellIndex = 0;
                    for (RecordImportFldInfo recordImportFldInfo : fldTemplateInfo) {
                        Cell excelRowCell = excelRow.getCell(rowCellIndex);
                        String cellValue = this.getCellValue(excelRowCell);

                        ReportJobData reportJobData = new ReportJobData();
                        reportJobData.setRecord_data(Strings.nullToEmpty(cellValue));
                        reportJobData.setColum_id(columIdIndex);
                        reportJobData.setFld_id(recordImportFldInfo.getFld_id());
                        reportJobData.setUnit_id(unitTemplateInfo.getUnit_id());
                        reportJobData.setReport_id(new Integer(reportId));

                        recordProcessDao.createRcdReortJobData(reportJobData,jobId);
//                        importDataList.add(reportJobData);
                        rowCellIndex++;
                    }
                }
                columIdIndex++;
            }
        }
    }

    private String getCellValue(Cell excelRowCell){
        String cellValue = null;
        if(excelRowCell!=null){
            try{
                cellValue = excelRowCell.getStringCellValue();
            }catch (IllegalStateException strException){
                try{
                    cellValue = new Double(excelRowCell.getNumericCellValue()).toString();
                }catch (IllegalStateException numberException){
                    try{
                        SimpleDateFormat format = new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss");
                        cellValue = format.format(excelRowCell.getDateCellValue());
                    }catch (IllegalStateException dateException){
                        try{
                            cellValue = String.valueOf(excelRowCell.getBooleanCellValue());
                        }catch (IllegalStateException booleanException){
                            try{
                                cellValue = excelRowCell.getRichStringCellValue().getString();
                            }catch (IllegalStateException richException){
                                richException.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return cellValue;
    }

}
