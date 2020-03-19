package com.datarecord.webapp.sys.origin.service.impl;

import com.AbstractTestService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import org.junit.Test;

import javax.annotation.Resource;

import static org.junit.Assert.*;

public class OriginServiceImpTest extends AbstractTestService {

    @Resource
    private OriginService originService;

    @Test
    public void getAllOriginTree() {
        Origin result = originService.getAllOriginTree();
    }
}