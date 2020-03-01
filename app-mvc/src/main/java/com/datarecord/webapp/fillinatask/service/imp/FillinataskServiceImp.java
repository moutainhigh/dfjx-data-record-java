package com.datarecord.webapp.fillinatask.service.imp;

import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.fillinatask.bean.*;
import com.datarecord.webapp.fillinatask.dao.FillinataskDao;
import com.datarecord.webapp.fillinatask.service.FillinataskService;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobUnitConfig;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
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
    private OriginService originService;


    @Override
    public PageResult rcdjobconfiglist(int currPage, int pageSize, String job_name, String job_status,Origin userOrigin) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        job_name = Strings.emptyToNull(job_name);
        job_status = Strings.emptyToNull(job_status);
        List<Origin> childrenOrigins = originService.checkAllChildren(userOrigin.getOrigin_id());
        childrenOrigins.add(0,userOrigin);
        List<Integer> originIds  = new ArrayList<>();
        for (Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id());
        }
        Page<Fillinatask> contactPageDatas = fillinataskDao.rcdjobconfiglist(currPage, pageSize,job_name,job_status,originIds);
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
            if(Strings.isNullOrEmpty(jobInterval.getJob_interval_start())||Strings.isNullOrEmpty(jobInterval.getJob_interval_end())){
                continue;
            }else{
                jobInterval.setJob_id(jobConfig.getJob_id());
                fillinataskDao.saveJobInterval(jobInterval);
            }
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
    @Transactional(rollbackFor = Exception.class)
    public void updateJobConfig(JobConfig jobConfig) {
        fillinataskDao.updatercdjobconfig(jobConfig);
        fillinataskDao.removeIntervals(jobConfig.getJob_id());
        List<JobInteval> intervals = jobConfig.getJob_intervals();
        if(intervals!=null){
            for (JobInteval interval : intervals) {
                if(Strings.isNullOrEmpty(interval.getJob_interval_start())||Strings.isNullOrEmpty(interval.getJob_interval_end())){
                    continue;
                }else{
                    interval.setJob_id(jobConfig.getJob_id());
                    fillinataskDao.saveJobInterval(interval);
                }

            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeJobIntervals(String jobId){
        fillinataskDao.removeIntervals(new Integer(jobId));
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
    @Transactional(rollbackFor = Exception.class)
    public void updateRcdJobUnitConfigyi(String jobunitid,String job_id) {
        /*jobunitid.substring(1);
        jobunitid.substring(0,jobunitid.length()-1);*/
        fillinataskDao.updateRcdJobUnitConfigsuo(job_id);
        String[] split = jobunitid.split(",");
        for (String job_unit_id : split){
            fillinataskDao.updateRcdJobUnitConfigyi(job_unit_id,job_id);
        }
    }


    @Override
    public List<RcdJobPersonAssign> huixianrcdjobpersonassign(String job_id) {
        return fillinataskDao.huixianrcdjobpersonassign(job_id);
}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletercdjobconfig(String job_id) {
        fillinataskDao.deletercdjobconfig(job_id);    //填报任务删除
        fillinataskDao.deletercdjobpersonassign(job_id);    //填报人维护删除
        fillinataskDao.deleteRcdJobUnitConfigsuo(job_id);    //任务关连填报组删除
        fillinataskDao.removeIntervals(new Integer(job_id));
    }



    @Override
    public JobConfig selectrcdjobconfigjobid(String job_id) {
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

    @Override
    public List<JobInteval> getJobIntevals(String job_id){
        return fillinataskDao.getJobIntevals(job_id);
    }
}
