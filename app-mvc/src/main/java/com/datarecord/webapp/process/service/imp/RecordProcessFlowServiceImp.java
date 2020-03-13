package com.datarecord.webapp.process.service.imp;

import com.datarecord.enums.JobConfigStatus;
import com.datarecord.enums.ReportStatus;
import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.dao.IRecordProcessFlowDao;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.process.service.RecordProcessFlowService;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.exception.runtime.WorkbenchRuntimeException;
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
    
    @Override
    public PageResult pageJob(String currPage, String pageSize, String reportStatus, String reportName, String reportOrigin) {
        //查询当前用户是否有审批权限
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        String userType = user.getUser_type();
        if(Strings.isNullOrEmpty(userType)&&!"0".equals(userType)){
            throw new WorkbenchRuntimeException("当前用户无审批权限",new RuntimeException());
        }
        //获取当前用户所属机构
        Origin userOrigin = originService.getOriginByUser(user.getUser_id());
        
        //获取当前用户所属机构以及其下属机构
        List<Origin> allOrigin = originService.listAllOrigin();
        List<Origin> childrenOrigins = null;
        if(Strings.isNullOrEmpty(reportOrigin)){
            childrenOrigins = originService.checkoutSons(userOrigin.getOrigin_id(), allOrigin);
            childrenOrigins.add(0,userOrigin);
        }else{
            childrenOrigins = originService.checkoutSons(new Integer(reportOrigin), allOrigin);
            Origin queryOrigin = new Origin();
            queryOrigin.setOrigin_id(new Integer(reportOrigin));
            childrenOrigins.add(0,queryOrigin);
        }

        //获取有权限机构下的所有已提交的报表
        Page<ReportJobInfo> resultDatas = recordProcessFlowDao.pageReviewDatas(
                new Integer(currPage), new Integer(pageSize), childrenOrigins,
                Strings.emptyToNull(reportStatus),
                Strings.emptyToNull(reportName)
                );

        PageResult pageResult = PageResult.pageHelperList2PageResult(resultDatas);

        List<ReportJobInfo> dataList = pageResult.getDataList();
        Date currDate = new Date();

        for (ReportJobInfo reportCustomer : dataList) {
            Date startDate = reportCustomer.getJob_start_dt();
            Date endDate = reportCustomer.getJob_end_dt();

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
    public void reviewJobItems(String jobId, String reviewStatus) {
        JobConfig jobConfig = recordProcessDao.getJobConfigByJobId(jobId);
        List<JobUnitConfig> jobUnits = jobConfig.getJobUnits();

        recordProcessFlowDao.changeJobConfig(jobId,String.valueOf(JobConfigStatus.getJobConfigStatus(reviewStatus).value()));

        for (JobUnitConfig jobUnit : jobUnits) {
            this.reviewUnit(String.valueOf(jobUnit.getJob_unit_id()),reviewStatus);
        }
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

