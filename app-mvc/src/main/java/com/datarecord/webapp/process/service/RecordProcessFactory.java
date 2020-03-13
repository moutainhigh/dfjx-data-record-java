package com.datarecord.webapp.process.service;

import com.datarecord.enums.JobUnitType;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public class RecordProcessFactory {

    private static RecordProcessService recordProcessService;
    private static RecordProcessService simpleRecordProcessService;
    private static RecordProcessService gridRecordProcessService;

    private RecordProcessFactory(){

    }

    public static final RecordProcessService RecordProcessSerice(){
        WebApplicationContext springWac = ContextLoader.getCurrentWebApplicationContext();

        if(recordProcessService==null){
            recordProcessService = (RecordProcessService) springWac.getBean("recordProcessService");
        }

        return recordProcessService;
    }

    public static final RecordProcessService RecordProcessSerice(JobUnitType jobUnitType){
        WebApplicationContext springWac = ContextLoader.getCurrentWebApplicationContext();

        if(jobUnitType.equals(JobUnitType.GRID)){
            if(gridRecordProcessService==null){
                gridRecordProcessService = (RecordProcessService) springWac.getBean("gridRecordProcessService");
            }
            return gridRecordProcessService;
        }

        if(jobUnitType.equals(JobUnitType.SIMPLE)){
            if(simpleRecordProcessService==null){
                simpleRecordProcessService = (RecordProcessService) springWac.getBean("simpleRecordProcessService");
            }
            return simpleRecordProcessService;
        }

        if(recordProcessService==null){
            recordProcessService = (RecordProcessService) springWac.getBean("recordProcessService");
        }

        return recordProcessService;
    }

}
