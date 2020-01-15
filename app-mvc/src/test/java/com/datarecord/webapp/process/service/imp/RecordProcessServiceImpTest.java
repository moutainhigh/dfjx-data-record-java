package com.datarecord.webapp.process.service.imp;

import com.AbstractTestService;
import com.datarecord.webapp.process.service.RecordProcessService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class RecordProcessServiceImpTest extends AbstractTestService {

    @Resource
    private RecordProcessService recordProcessService;

    @Test
    public void makeJob() {
        recordProcessService.makeJob("1");

    }
}