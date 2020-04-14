package com.datarecord.webapp.process.service.imp;

import com.AbstractTestService;
import com.datarecord.webapp.process.entity.ExportParams;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobUnitConfig;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.datarecord.webapp.process.service.RecordProcessFlowService;
import com.datarecord.webapp.process.service.RecordProcessService;
import org.junit.Test;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class AbstractRecordProcessServiceImpTest extends AbstractTestService {

    @Resource
    private RecordProcessFlowService recordProcessFlowService;

    @Test
    public void exportRecordData() {
        ExportParams exportParams = new ExportParams();
        exportParams.setReport_id("7");
        JobConfig jobConfig = new JobConfig();
        jobConfig.setJob_id(5);
        List<JobUnitConfig> jobUnits =new ArrayList<>();
        jobConfig.setJobUnits(jobUnits);
        JobUnitConfig unit1 = new JobUnitConfig();
        JobUnitConfig unit2 = new JobUnitConfig();
        List<ReportFldConfig> configList1 = new ArrayList<>();
        List<ReportFldConfig> configList2 = new ArrayList<>();
        unit1.setUnitFlds(configList1);
        unit1.setJob_unit_id(11);
        unit2.setUnitFlds(configList2);
        unit2.setJob_unit_id(12);
        ReportFldConfig fldConfig7 = new ReportFldConfig();
        ReportFldConfig fldConfig8 = new ReportFldConfig();
        ReportFldConfig fldConfig9 = new ReportFldConfig();
        ReportFldConfig fldConfig10 = new ReportFldConfig();
        fldConfig7.setFld_id(13);
        fldConfig8.setFld_id(14);
        fldConfig9.setFld_id(15);
        fldConfig10.setFld_id(16);
        configList1.add(fldConfig7);
        configList1.add(fldConfig8);
        configList1.add(fldConfig9);
        configList1.add(fldConfig10);

        ReportFldConfig fldConfigA = new ReportFldConfig();
        ReportFldConfig fldConfigB = new ReportFldConfig();
        ReportFldConfig fldConfigC = new ReportFldConfig();
        fldConfigA.setFld_id(13);
        fldConfigB.setFld_id(15);
        fldConfigC.setFld_id(16);
        configList2.add(fldConfigA);
        configList2.add(fldConfigB);
        configList2.add(fldConfigC);

        jobUnits.add(unit1);
        jobUnits.add(unit2);
        jobConfig.setJobUnits(jobUnits);
        exportParams.setJobConfig(jobConfig);
        recordProcessFlowService.exportRecordData(exportParams);
    }
}