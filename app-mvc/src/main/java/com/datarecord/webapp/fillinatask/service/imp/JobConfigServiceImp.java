package com.datarecord.webapp.fillinatask.service.imp;

import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.fillinatask.bean.*;
import com.datarecord.webapp.fillinatask.dao.IJobConfigDao;
import com.datarecord.webapp.fillinatask.service.JobConfigService;
import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobUnitConfig;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("jobConfigService")
public class JobConfigServiceImp implements JobConfigService {

    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private IJobConfigDao jobConfigDao;

    @Autowired
    private IRecordProcessDao recordProcessDao;

    @Autowired
    private OriginService originService;


    @Override
    public PageResult rcdjobconfiglist(int currPage, int pageSize, String job_name, String job_status, String user_id) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        job_name = Strings.emptyToNull(job_name);
        job_status = Strings.emptyToNull(job_status);
       /* List<Origin> childrenOrigins = originService.checkAllChildren(userOrigin.getOrigin_id());
        childrenOrigins.add(0,userOrigin);
        List<Integer> originIds  = new ArrayList<>();
        for (Origin childrenOrigin : childrenOrigins) {
            originIds.add(childrenOrigin.getOrigin_id());
        }*/
        Page<Fillinatask> contactPageDatas = jobConfigDao.rcdjobconfiglist(currPage, pageSize,job_name,job_status,user_id);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveJobConfig(JobConfig jobConfig) {
        jobConfig.setJob_start_dt(jobConfig.getJob_start_dt());
        jobConfig.setJob_end_dt(jobConfig.getJob_end_dt());
        jobConfigDao.saveJobConfig(jobConfig);
        List<JobInteval> jobIntervals = jobConfig.getJob_intervals();
        for (JobInteval jobInterval : jobIntervals) {
            if(Strings.isNullOrEmpty(jobInterval.getJob_interval_start())||Strings.isNullOrEmpty(jobInterval.getJob_interval_end())){
                continue;
            }else{
                jobInterval.setJob_id(jobConfig.getJob_id());
                jobConfigDao.saveJobInterval(jobInterval);
            }
        }
    }

    @Override
    public void insertrcdjobpersonassign(String job_id, String userid) {
       /* userid.substring(1);
        userid.substring(0,userid.length()-1);*/
        String[] split = userid.split(",");
        for (String user_id : split){
            jobConfigDao.saveJobPersonAssign(job_id,user_id);
        }
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobConfig(JobConfig jobConfig) {
        jobConfigDao.updatercdjobconfig(jobConfig);
        jobConfigDao.removeIntervals(jobConfig.getJob_id());
        List<JobInteval> intervals = jobConfig.getJob_intervals();
        if(intervals!=null){
            for (JobInteval interval : intervals) {
                if(Strings.isNullOrEmpty(interval.getJob_interval_start())||Strings.isNullOrEmpty(interval.getJob_interval_end())){
                    continue;
                }else{
                    interval.setJob_id(jobConfig.getJob_id());
                    jobConfigDao.saveJobInterval(interval);
                }

            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeJobIntervals(String jobId){
        jobConfigDao.removeIntervals(new Integer(jobId));
    }



    @Override
    public List<RcdJobUnitConfig> selectRcdJobUnitConfig(String job_id) {
        return jobConfigDao.selectRcdJobUnitConfig(job_id);
    }

    @Override
    public List<RcdJobUnitConfig> selectRcdJobUnitConfigyi(String job_id) {
        return jobConfigDao.selectRcdJobUnitConfigyi(job_id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRcdJobUnitConfigyi(String jobunitid,String job_id) {
        /*jobunitid.substring(1);
        jobunitid.substring(0,jobunitid.length()-1);*/
        jobConfigDao.updateRcdJobUnitConfigsuo(job_id);
        String[] split = jobunitid.split(",");
        for (String job_unit_id : split){
            jobConfigDao.updateRcdJobUnitConfigyi(job_unit_id,job_id);
        }
    }


    @Override
    public List<RcdJobPersonAssign> huixianrcdjobpersonassign(String job_id) {
        return jobConfigDao.huixianrcdjobpersonassign(job_id);
}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletercdjobconfig(String job_id) {
        jobConfigDao.deletercdjobconfig(job_id);    //填报任务删除
        jobConfigDao.delJobPersonAssign(job_id);    //填报人维护删除
        jobConfigDao.deleteRcdJobUnitConfigsuo(job_id);    //任务关连填报组删除
        jobConfigDao.removeIntervals(new Integer(job_id));
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateByIdConfig(String job_id) {
        jobConfigDao.updsoftDelJobConfig(job_id);    //填报任务软删除
        jobConfigDao.updateRcdjobpersonassign(job_id);    //填报人维护软删除
        jobConfigDao.updateByidRcdJobUnitConfigsuo(job_id);    //任务关连填报组软删除
        jobConfigDao.updateRcdReportJob(job_id);    //已下发的填报状态修改
        jobConfigDao.removeIntervals(new Integer(job_id));
    }



    @Override
    public JobConfig getJobConfig(String job_id) {
        return   jobConfigDao.selectrcdjobconfigjobid(job_id);
    }

    @Override
    public void deletercdjobpersonassignbyuseridandjobid(String job_id, String user_id) {
        jobConfigDao.deletercdjobpersonassignbyuseridandjobid(job_id,user_id);
    }

    @Override
    public String selectrcdjobconfig(Integer jobid) {
        return jobConfigDao.selectrcdjobconfig(jobid);
    }

    @Override
    public String selectrcdjobunitconfig(String unitId) {
        return jobConfigDao.selectrcdjobunitconfig(unitId);
    }


    @Override
    public List<Lieming> selectrcdreportdatajob(int jobid, int reportid, String unitId, List fldids) {
        return jobConfigDao.selectrcdreportdatajob(jobid,reportid,unitId,fldids);
    }

    @Override
    public void fillInTaskApprovalByJobid(String job_id) {
        jobConfigDao.approveJobConfig(job_id);
    }

    @Override
    public List<JobUnitConfig> taskDetailsjobUnitConfig(String job_id) {
        return  jobConfigDao.taskDetailsjobUnitConfig(job_id);
    }

    @Override
    public List<ReportFldConfig> taskDetailsreportFldConfig(String job_id) {
        return jobConfigDao.taskDetailsreportFldConfig(job_id);
    }

    @Override
    public List<JobInteval> getJobIntevals(String job_id){
        return jobConfigDao.getJobIntevals(job_id);
    }
}
