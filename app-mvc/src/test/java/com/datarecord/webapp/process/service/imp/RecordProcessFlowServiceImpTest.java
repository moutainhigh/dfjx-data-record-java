package com.datarecord.webapp.process.service.imp;

import com.AbstractTestService;
import com.datarecord.webapp.process.service.RecordProcessFlowService;
import com.datarecord.webapp.process.service.RecordProcessService;
import org.junit.Test;

import javax.annotation.Resource;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordProcessFlowServiceImpTest extends AbstractTestService {

    @Resource
    private RecordProcessFlowService recordProcessFlowService;

    @Test
    public void pageReportDatas() {
        Map<String,String> queryParams = new HashMap<>();
//        recordProcessFlowService.pageReportDatas(new BigInteger("1101000"),"1","10",queryParams);
        recordProcessFlowService.pageReportDatas(null,"1","10",queryParams);
    }
}