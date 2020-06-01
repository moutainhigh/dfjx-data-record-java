package com.datarecord.webapp.fillinatask.service;

import com.datarecord.webapp.fillinatask.bean.JobInteval;
import com.datarecord.webapp.fillinatask.bean.Lieming;
import com.datarecord.webapp.fillinatask.bean.RcdJobPersonAssign;
import com.datarecord.webapp.fillinatask.bean.RcdJobUnitConfig;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobUnitConfig;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.webapp.support.page.PageResult;

import java.util.List;
import java.util.Map;

public interface JobConfigService {

    PageResult jobConfigList(int currPage, int pageSize, String job_name, String job_status, String user_id);

    void saveJobConfig(JobConfig jobConfig);

    void saveJobPersons(String job_id, String userid);

    void updateJobConfig(JobConfig jobConfig);

    List<RcdJobUnitConfig> getJobUnitConfig(String job_id);

    List<RcdJobUnitConfig> selectRcdJobUnitConfigyi(String job_id);

    void updateRcdJobUnitConfigyi(String jobunitid, String job_id);

    List<RcdJobPersonAssign> huixianrcdjobpersonassign(String job_id);

    void deletercdjobconfig(String job_id);

    JobConfig getJobConfig(String job_id);

    void deletercdjobpersonassignbyuseridandjobid(String job_id, String user_id);

    String selectrcdjobconfig(Integer jobid);

    String selectrcdjobunitconfig(String unitId);

    List<Lieming> selectrcdreportdatajob(int jobid, int reportid, String unitId, List fldids);

    void fillInTaskApprovalByJobid(String job_id);

    List<JobUnitConfig> taskDetailsjobUnitConfig(String job_id);

    List<ReportFldConfig> taskDetailsreportFldConfig(String job_id);

    List<JobInteval> getJobIntevals(String job_id);

    Map<Integer, List<JobInteval>> allJobIntevals();

    void removeJobIntervals(String jobId);

    void updateByIdConfig(String job_id);
}
