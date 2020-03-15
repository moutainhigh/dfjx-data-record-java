package com.datarecord.webapp.reportinggroup.service.imp;

import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.reportinggroup.bean.*;
import com.datarecord.webapp.reportinggroup.dao.ReportingGroupDao;
import com.datarecord.webapp.reportinggroup.service.ReportingGroupService;
import com.datarecord.webapp.utils.EntityTree;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service("reportingGroupService")
public class ReportingGroupServiceImp  implements ReportingGroupService {

    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private ReportingGroupDao reportingGroupDao;



    @Override
    public List<rcdJobConfig> leftrcdjobconfig(String origin_id) {
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
        return reportingGroupDao.leftrcdjobconfig(originid);
    }



    @Override
    public PageResult rcdjobunitconfiglist(int currPage, int pageSize, String job_id) {
        logger.debug("当前页码:{},页面条数:{}",currPage,pageSize);
        Page<ReportingGroup> contactPageDatas = reportingGroupDao.rcdjobunitconfiglist(currPage, pageSize,job_id);
        PageResult pageContactResult = PageResult.pageHelperList2PageResult(contactPageDatas);
        logger.debug("获取到的分页结果数据 --> {}",pageContactResult);
        return pageContactResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletercdjobunitconfig(String job_unit_id) {
        reportingGroupDao.deletercdjobunitconfig(job_unit_id);
        reportingGroupDao.deletercdjobunitfld(job_unit_id);
        reportingGroupDao.deleteReportingInterval(null,new Integer(job_unit_id));
    }

    @Override
    public void rcdjobunitfld(String fld_id, String jobunitid) {
       /* jobunitid.substring(1);
        jobunitid.substring(0,jobunitid.length()-1);*/
        String[] split = fld_id.split(",");
        for (String fldid : split){
            reportingGroupDao.rcdjobunitfld(fldid,jobunitid);
        }
    }

    @Override
    public void rcdjobunitflddelete(String jobunitid) {
        reportingGroupDao.rcdjobunitflddelete(jobunitid);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertrcdjobunitconfig(ReportingGroup reportingGroup) {
        reportingGroupDao.insertrcdjobunitconfig(reportingGroup);
        List<ReportGroupInterval> reportingGroupIntervals = reportingGroup.getReportGroupIntervals();
        if(reportingGroupIntervals!=null){
            for (ReportGroupInterval reportingGroupInterval : reportingGroupIntervals) {
                reportingGroupInterval.setJob_id(reportingGroup.getJob_id());
                reportingGroupInterval.setJob_unit_id(reportingGroup.getJob_unit_id());
                reportingGroupDao.saveReportingInterval(reportingGroupInterval);
            }
        }
        RcdJobUnitFlow unitFlow = reportingGroup.getRcdJobUnitFlow();
        unitFlow.setJob_id(reportingGroup.getJob_id());
        unitFlow.setUnit_id(reportingGroup.getJob_unit_id());
        reportingGroupDao.saveUnitFlow(unitFlow);
    }

    @Override
    public void updatercdjobunitconfig(ReportingGroup reportingGroup) {
        reportingGroupDao.updatercdjobunitconfig(reportingGroup);
        List<ReportGroupInterval> reportingGroupIntervals = reportingGroup.getReportGroupIntervals();
        reportingGroupDao.deleteReportingInterval(reportingGroup.getJob_id(),reportingGroup.getJob_unit_id());
        if(reportingGroupIntervals!=null){
            for (ReportGroupInterval reportingGroupInterval : reportingGroupIntervals) {
                reportingGroupInterval.setJob_id(reportingGroup.getJob_id());
                reportingGroupInterval.setJob_unit_id(reportingGroup.getJob_unit_id());
                reportingGroupDao.saveReportingInterval(reportingGroupInterval);
            }
        }

        RcdJobUnitFlow unitFlow = reportingGroupDao.getUnitFLow(reportingGroup.getJob_id(),String.valueOf(reportingGroup.getJob_unit_id()));

        if(unitFlow!=null){
            unitFlow = reportingGroup.getRcdJobUnitFlow();
            unitFlow.setUnit_id(reportingGroup.getJob_unit_id());
            unitFlow.setJob_id(reportingGroup.getJob_id());
            reportingGroupDao.editUnitFlow(unitFlow);
        }else{
            unitFlow = reportingGroup.getRcdJobUnitFlow();
            unitFlow.setJob_id(reportingGroup.getJob_id());
            unitFlow.setUnit_id(reportingGroup.getJob_unit_id());
            reportingGroupDao.saveUnitFlow(unitFlow);
        }

    }

    @Override
    public ReportingGroup selectrcdjobunitconfigByjobunitid(String job_unit_id) {
        ReportingGroup reportGroup = reportingGroupDao.selectrcdjobunitconfigByjobunitid(job_unit_id);
        if(reportGroup!=null){
            List<ReportGroupInterval> reportGroupIntervals = reportingGroupDao.getReportGroupInterval(reportGroup.getJob_id(),reportGroup.getJob_unit_id());
            reportGroup.setReportGroupIntervals(reportGroupIntervals);
        }
        RcdJobUnitFlow unitFlow = reportingGroupDao.getUnitFLow(reportGroup.getJob_id(),job_unit_id);
        reportGroup.setRcdJobUnitFlow(unitFlow);
        return reportGroup;
    }

    @Override
    public List<RcdJobUnitFld> selectrcdjobunitfld(String origin_id, String job_unit_id) {
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
        return reportingGroupDao.selectrcdjobunitfld(originid,job_unit_id);
    }


  /*  @Override
    public List<RcdJobUnitFld> selectrcdjobunitfld(String job_unit_id) {
        return reportingGroupDao.selectrcdjobunitfld(job_unit_id);
    }
    */

}
