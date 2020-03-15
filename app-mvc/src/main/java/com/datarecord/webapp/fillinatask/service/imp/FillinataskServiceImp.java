package com.datarecord.webapp.fillinatask.service.imp;

import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.fillinatask.bean.*;
import com.datarecord.webapp.fillinatask.dao.FillinataskDao;
import com.datarecord.webapp.fillinatask.service.FillinataskService;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobUnitConfig;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.datarecord.webapp.reportinggroup.dao.ReportingGroupDao;
import com.datarecord.webapp.utils.EntityTree;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("fillinataskService")
public class FillinataskServiceImp implements FillinataskService {

    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private FillinataskDao fillinataskDao;

    @Autowired
    private ReportingGroupDao reportingGroupDao;

    @Override
    public PageResult rcdjobconfiglist(int currPage, int pageSize, String job_name, String job_status,String origin_id) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        job_name = Strings.emptyToNull(job_name);
        job_status = Strings.emptyToNull(job_status);
        List<EntityTree> lists =  reportingGroupDao.selectoriginid();
        List<String> lsls = new ArrayList<String>();
        for (EntityTree x : lists) {
            if((null != x.getpId() && !"".equals(x.getpId()))  ||  (null != x.getId() && !"".equals(x.getId()) ) ){
                if(origin_id.equals(x.getpId())){
                    lsls.add(x.getId());
                }else if (origin_id.equals(x.getId())){
                    lsls.add(x.getId());
                }
            }
        }
        String originid ="";
        if(lsls.size() > 0){

            for (String id : lsls){
                originid += ",";
                originid += id;
            }
            originid =  originid.substring(1);
        }
        Page<Fillinatask> contactPageDatas = fillinataskDao.rcdjobconfiglist(currPage, pageSize,job_name,job_status,originid);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveJobConfig(JobConfig jobConfig) {
        jobConfig.setJob_start_dt(jobConfig.getJob_start_dt());
        jobConfig.setJob_end_dt(jobConfig.getJob_end_dt());
        fillinataskDao.saveJobConfig(jobConfig);
        List<JobInteval> jobIntervals = jobConfig.getJob_intervals();
        for (JobInteval jobInterval : jobIntervals) {
            jobInterval.setJob_id(jobConfig.getJob_id());
            fillinataskDao.saveJobInterval(jobInterval);
        }
    }

    @Override
    public void insertrcdjobpersonassign(String job_id, String userid) {
       /* userid.substring(1);
        userid.substring(0,userid.length()-1);*/
        String[] split = userid.split(",");
        for (String user_id : split){
            fillinataskDao.insertrcdjobpersonassign(job_id,user_id);
        }
    }

    @Override
    public void deletercdjobpersonassign(String job_id) {
        fillinataskDao.deletercdjobpersonassign(job_id);
    }

    @Override
    public void updateJobConfig(String job_id, String job_name, String job_start_dt, String job_end_dt ) {
        fillinataskDao.updatercdjobconfig(job_id,job_name,job_start_dt,job_end_dt);
    }

    @Override
    public List<RcdJobUnitConfig> selectRcdJobUnitConfig(String job_id) {
        return fillinataskDao.selectRcdJobUnitConfig(job_id);
    }

    @Override
    public List<RcdJobUnitConfig> selectRcdJobUnitConfigyi(String job_id) {
        return fillinataskDao.selectRcdJobUnitConfigyi(job_id);
    }

    @Override
    public void updateRcdJobUnitConfigyi(String jobunitid,String job_id) {
        /*jobunitid.substring(1);
        jobunitid.substring(0,jobunitid.length()-1);*/
        String[] split = jobunitid.split(",");
        for (String job_unit_id : split){
            fillinataskDao.updateRcdJobUnitConfigyi(job_unit_id,job_id);
        }
    }

    @Override
    public void updateRcdJobUnitConfigsuo(String job_id) {
        fillinataskDao.updateRcdJobUnitConfigsuo(job_id);
    }

    @Override
    public List<RcdJobPersonAssign> huixianrcdjobpersonassign(String job_id) {
        return fillinataskDao.huixianrcdjobpersonassign(job_id);
}

    @Override
    public void deletercdjobconfig(String job_id) {
        fillinataskDao.deletercdjobconfig(job_id);
    }

    @Override
    public void deleteRcdJobUnitConfigsuo(String job_id) {
        fillinataskDao.deleteRcdJobUnitConfigsuo(job_id);
    }

    @Override
    public List<Fillinatask> selectrcdjobconfigjobid(String job_id) {
        return   fillinataskDao.selectrcdjobconfigjobid(job_id);
    }

    @Override
    public void deletercdjobpersonassignbyuseridandjobid(String job_id, String user_id) {
        fillinataskDao.deletercdjobpersonassignbyuseridandjobid(job_id,user_id);
    }

    @Override
    public String selectrcdjobconfig(Integer jobid) {
        return fillinataskDao.selectrcdjobconfig(jobid);
    }

    @Override
    public String selectrcdjobunitconfig(String unitId) {
        return fillinataskDao.selectrcdjobunitconfig(unitId);
    }


    @Override
    public List<Lieming> selectrcdreportdatajob(int jobid, int reportid, String unitId, String fldids) {
        return fillinataskDao.selectrcdreportdatajob(jobid,reportid,unitId,fldids);
    }

    @Override
    public void fillInTaskApprovalByJobid(String job_id) {
        fillinataskDao.approveJobConfig(job_id);
    }

    @Override
    public List<JobUnitConfig> taskDetailsjobUnitConfig(String job_id) {
        return  fillinataskDao.taskDetailsjobUnitConfig(job_id);
    }

    @Override
    public List<ReportFldConfig> taskDetailsreportFldConfig(String job_id) {
        return fillinataskDao.taskDetailsreportFldConfig(job_id);
    }
}
