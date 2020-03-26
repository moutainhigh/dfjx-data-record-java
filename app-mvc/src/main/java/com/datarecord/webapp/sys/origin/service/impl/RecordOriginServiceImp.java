package com.datarecord.webapp.sys.origin.service.impl;

import com.datarecord.webapp.sys.origin.dao.ISubmitauthorityDao;
import com.datarecord.webapp.sys.origin.entity.RecordOrigin;
import com.datarecord.webapp.sys.origin.service.RecordOriginService;
import com.datarecord.webapp.sys.origin.tree.EntityTree;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("submitauthority")
public class RecordOriginServiceImp implements RecordOriginService {

    @Autowired
    private ISubmitauthorityDao submitauthorityDao;

//    @Autowired
//    private ReportCustomerService reportCustomerService;

    @Override
    public List<EntityTree> listAllRecordOrigin() {
        return submitauthorityDao.listAllSubmitauthority();
    }

    @Override
    public PageResult listRecordOrigin(int currPage, int pageSize) {
        Page<RecordOrigin> recordOriginPage = submitauthorityDao.listSubmitauthority(currPage, pageSize);
        PageResult pageResult = PageResult.pageHelperList2PageResult(recordOriginPage);
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRecordOrigin(RecordOrigin recordOrigin) {
        if(recordOrigin.getOrigin_id()!=null){
            submitauthorityDao.updateSubmitauthority(recordOrigin);
            String originType = recordOrigin.getOrigin_type();
            //检查该机构下的报表，如果报表类型与修改后的机构类型不符。将报表置为失效
            List<BigInteger> origins = new ArrayList<>();
            origins.add(recordOrigin.getOrigin_id());
//            List<ReportCustomer> reportCustomers  = reportCustomerService.allReportForOrigin(recordOrigin.getOrigin_id().toString());
//            if(reportCustomers!=null){
//                for (ReportCustomer reportCustomer : reportCustomers) {
//                    String oldStatus = reportCustomer.getReport_status();
//
//                    String reportType = reportCustomer.getReport_type();
//                    if(reportType.equals("0")){//所有企业都要填的报表
//                        if(ReportStatus.REMOVE.compareTo(oldStatus)){
//                            reportCustomerService.updateReportCustomerStatus(
//                                    reportCustomer.getReport_id().toString(), ReportStatus.NORMAL);
//                        }
//                    }else{
//                        if(!reportType.equals(originType)){
//                            reportCustomerService.updateReportCustomerStatus(
//                                    reportCustomer.getReport_id().toString(), ReportStatus.REMOVE);
//                        }else{
//                            if(ReportStatus.REMOVE.compareTo(oldStatus)){
//                                reportCustomerService.updateReportCustomerStatus(
//                                        reportCustomer.getReport_id().toString(), ReportStatus.NORMAL);
//                            }
//                        }
//                    }
//                }
//            }

        }else{
            submitauthorityDao.addSubmitauthority(recordOrigin);
        }
    }

    @Override
    public void deleteById(String originId) {
        //获取originId下的所有报送机构
        Map<String, Object> originTree = submitauthorityDao.getOriginById(originId);
        List finalOriginList = new ArrayList();
        checkOrigins(originTree,finalOriginList);
        submitauthorityDao.deleteByListId(finalOriginList);
    }

    @Override
    public List<String> getReportOriginForOrganizationUser(int currUserId) {
        return submitauthorityDao.getOriginIdListByUserId(currUserId);
    }

    private void checkOrigins(Map<String, Object> origin,List finalOriginList){
        List<Map<String, Object>> children = (List)origin.get("childrens");
        finalOriginList.add(origin.get("origin_id"));
        if(children!=null&&children.size()>0){
            children.forEach(child->{
                checkOrigins(child,finalOriginList);
            });
        }
    }
}
