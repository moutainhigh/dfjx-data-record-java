package com.datarecord.webapp.process.service.imp;

import com.datarecord.webapp.process.dao.IRecordProcessFlowDao;
import com.datarecord.webapp.process.entity.ReportJobData;
import com.datarecord.webapp.process.entity.ReportJobInfo;
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

import java.util.List;

@Service("recordProcessFlowService")
public class RecordProcessFlowServiceImp implements RecordProcessFlowService {

    @Autowired
    private OriginService originService;

    @Autowired
    private IRecordProcessFlowDao recordProcessFlowDao;
    
    @Override
    public PageResult pageJob(String currPage, String pageSize) {
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
        List<Origin> childrenOrigins = originService.checkoutSons(userOrigin.getParent_origin_id(), allOrigin);
        childrenOrigins.add(userOrigin);
        //获取有权限机构下的所有已提交的报表
        Page<ReportJobInfo> resultDatas = recordProcessFlowDao.pageReviewDatas(new Integer(currPage),new Integer(pageSize),childrenOrigins);

        PageResult pageResult = PageResult.pageHelperList2PageResult(resultDatas);
        
        return pageResult;
    }
}
