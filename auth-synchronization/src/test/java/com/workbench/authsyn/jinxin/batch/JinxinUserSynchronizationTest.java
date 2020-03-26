package com.workbench.authsyn.jinxin.batch;

import com.AbstractTestService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class JinxinUserSynchronizationTest extends AbstractTestService {

    @Resource
    private  JinxinUserSynchronization jinxinUserSynchronization;

    @Test
    public void testDoSynUser(){
        jinxinUserSynchronization.doSyn();
    }

}