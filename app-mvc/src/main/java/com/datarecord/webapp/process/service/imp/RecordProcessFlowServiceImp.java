package com.datarecord.webapp.process.service.imp;

import com.datarecord.webapp.process.dao.IRecordProcessDao;
import com.datarecord.webapp.process.dao.IRecordProcessFlowDao;
import com.datarecord.webapp.process.entity.ReportJobInfo;
import com.datarecord.webapp.process.entity.ReportStatus;
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

import java.util.Date;
import java.util.List;

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
            childrenOrigins = originService.checkoutSons(userOrigin.getParent_origin_id(), allOrigin);
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
}
