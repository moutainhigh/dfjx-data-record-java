package com.datarecord.webapp.process.service.imp;

import com.datarecord.enums.FldConfigStatus;
import com.datarecord.enums.JobConfigStatus;
import com.datarecord.enums.ReportStatus;
import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.dao.IRecordProcessFlowDao;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessFlowService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.utils.DataRecordUtil;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.exception.runtime.WorkbenchRuntimeException;
import com.workbench.shiro.WorkbenchShiroUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service("recordProcessFlowService")
public class RecordProcessFlowServiceImp implements RecordProcessFlowService {

    @Autowired
    private OriginService originService;

    @Autowired
    private IRecordProcessFlowDao recordProcessFlowDao;

    @Autowired
    protected IRecordProcessDao recordProcessDao;

    /**
     * 审批列表,查询当前用户发布的填报任务.
     * 如果当前用户为系统管理员user_type=3,则可查询全部数据
     * @param currPage
     * @param pageSize
     * @param reportStatus
     * @param reportName
     * @param reportOrigin
     * @return
     */
    @Override
    public PageResult pageJob(String currPage, String pageSize, String reportStatus, String reportName, String reportOrigin) {
        //查询当前用户是否有审批权限
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        Integer userId = user.getUser_id();
        if(DataRecordUtil.isSuperUser()){//当前用户是否是超级管理员
            userId = null;
        }
        //获取当前用户所属机构
//        Origin userOrigin = originService.getOriginByUser(user.getUser_id());

        //获取有权限机构下的所有已提交的报表
        Page<ReportJobInfo> resultDatas = recordProcessFlowDao.pageReviewDatas(
                new Integer(currPage), new Integer(pageSize), userId,
                Strings.emptyToNull(reportStatus),
                Strings.emptyToNull(reportName)
                );

        PageResult pageResult = PageResult.pageHelperList2PageResult(resultDatas);

        List<ReportJobInfo> dataList = pageResult.getDataList();
        Date currDate = new Date();

        for (ReportJobInfo reportCustomer : dataList) {
            Date startDate = reportCustomer.getJob_start_dt();
            Date endDate = reportCustomer.getJob_end_dt();

            if(ReportStatus.REPORT_DONE.compareTo(reportCustomer.getRecord_status())){
                continue;
            }

            if(currDate.compareTo(startDate)<0){//未到填报日期
                reportCustomer.setRecord_status(ReportStatus.TOO_EARLY.getValueInteger());
            }
            if(currDate.compareTo(endDate)>0){//已过期
                reportCustomer.setRecord_status(ReportStatus.OVER_TIME.getValueInteger());
                recordProcessDao.changeRecordJobStatus(reportCustomer.getReport_id(),ReportStatus.OVER_TIME.getValueInteger());
            }
        }

        return pageResult;
    }

    @Override
    public void reviewFld(String fldId,String reviewStatus) {
        recordProcessFlowDao.reviewFld(fldId,reviewStatus);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewUnit(String unitId, String reviewStatus) {
        List<ReportFldConfig> flds = recordProcessDao.getFldByUnitId(unitId);
        for (ReportFldConfig fld : flds) {
            Integer fldId = fld.getFld_id();
            if(fld.getFld_status()==0){
                this.reviewFld(fldId.toString(),reviewStatus);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reviewJobItems( JobFlowLog jobFlowLog) {
        Integer jobId = jobFlowLog.getJob_id();
        JobConfig jobConfig = recordProcessDao.getJobConfigByJobId(jobId.toString());
//        String fldStatus = reviewStatus;
        Integer reviewStatusInt = new Integer(jobFlowLog.getJob_flow_status());
//        if(JobConfigStatus.APPROVE.compareWith(reviewStatusInt)){
//            fldStatus = FldConfigStatus.APPROVE.getValue();
//        }else if(JobConfigStatus.REJECT.compareWith(reviewStatusInt)){
//            fldStatus = FldConfigStatus.REJECT.getValue();
//        }

        List<JobUnitConfig> jobUnits = jobConfig.getJobUnits();
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        jobFlowLog.setJob_flow_user(user.getUser_id());
        jobFlowLog.setJob_flow_date(new Date());
        recordProcessFlowDao.changeJobConfig(jobId.toString(),jobFlowLog.getJob_flow_status().toString());
        recordProcessFlowDao.recordFlowLog(jobFlowLog);
//        for (JobUnitConfig jobUnit : jobUnits) {
//            this.reviewUnit(String.valueOf(jobUnit.getJob_unit_id()),fldStatus);
//        }
    }

    @Override
    public PageResult pageReviewJobConfigs(int user_id, String currPage, String pageSize, Map<String, String> queryParams) {
        List<Origin> childrenOrigins = new ArrayList<>();
        Object fldOrigin = queryParams.get("jobOrigin");
        if(fldOrigin!=null){
            Origin origin = new Origin();
            if(fldOrigin instanceof Double){
                Double fldOriginDb = (Double) fldOrigin;
                fldOriginDb.intValue();
                origin.setOrigin_id(fldOriginDb.intValue());
            }else if(fldOrigin instanceof String){
                origin.setOrigin_id(new Integer(String.valueOf(fldOrigin)));
            }
            childrenOrigins.add(origin);
        }else{
            childrenOrigins = this.checkAuthOrigins(user_id);
        }
        queryParams.put("jobName",Strings.emptyToNull(queryParams.get("jobName")));
        queryParams.put("jobStatus",Strings.emptyToNull(queryParams.get("jobStatus")));
        Page<JobConfig> reviewJobs =  recordProcessFlowDao.pageReviewJobs(currPage,pageSize,childrenOrigins,queryParams);
        PageResult pageResult = PageResult.pageHelperList2PageResult(reviewJobs);

        return pageResult;
    }

    @Override
    public PageResult pageReviewFlds(int user_id, String currPage, String pageSize, Map<String, String> queryParams) {
        List<Origin> childrenOrigins = new ArrayList<>();
        Object fldOrigin = queryParams.get("fldOrigin");
        if(fldOrigin!=null){
            Origin origin = new Origin();
            if(fldOrigin instanceof Double){
                Double fldOriginDb = (Double) fldOrigin;
                fldOriginDb.intValue();
                origin.setOrigin_id(fldOriginDb.intValue());
            }else if(fldOrigin instanceof String){
                origin.setOrigin_id(new Integer(String.valueOf(fldOrigin)));
            }
            childrenOrigins.add(origin);
        }else{
            childrenOrigins = this.checkAuthOrigins(user_id);
        }
        queryParams.put("fldName",Strings.emptyToNull(queryParams.get("fldName")));
        queryParams.put("fldStatus",Strings.emptyToNull(queryParams.get("fldStatus")));
        Page<ReportFldConfig> reportFldConfigPage =  recordProcessFlowDao.pageReviewFlds(currPage,pageSize,childrenOrigins,queryParams);
        PageResult pageResult = PageResult.pageHelperList2PageResult(reportFldConfigPage);

        return pageResult;
    }

    @Override
    public void subJobConfig(String jobId) {
        recordProcessFlowDao.changeJobConfig(jobId,String.valueOf(JobConfigStatus.REVIEW.value()));
    }

    @Override
    public List<JobFlowLog> getJobFlowLogs(String jobId) {
        List<JobFlowLog> flowLogs = recordProcessFlowDao.listJobFlowLogs(jobId);
        return flowLogs;
    }

    private List<Origin> checkAuthOrigins(Integer user_id){
        Origin userOrigin = originService.getOriginByUser(user_id);
        if(userOrigin==null){
            throw new WorkbenchRuntimeException("找不到当前用户对应的机构",new RuntimeException(""));
        }
        List<Origin> childrenOrigins = originService.checkAllChildren(userOrigin.getOrigin_id());
        childrenOrigins.add(0,userOrigin);
        return childrenOrigins;
    }
}

