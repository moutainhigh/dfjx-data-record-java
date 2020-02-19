package com.datarecord.webapp.process.service.imp;

import com.AbstractTestService;
import com.datarecord.webapp.process.service.RecordProcessService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class AbstractRecordProcessServiceImpTest extends AbstractTestService {

    @Resource
    private RecordProcessService recordProcessService;

    @Test
    public void makeJob() {
        recordProcessService.makeJob("1");

    }

    @Test
    public void pageJob() {
        recordProcessService.pageJob(2727024,"1","10");
    }
}