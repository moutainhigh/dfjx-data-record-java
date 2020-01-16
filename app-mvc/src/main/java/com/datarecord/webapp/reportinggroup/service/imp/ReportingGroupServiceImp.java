package com.datarecord.webapp.reportinggroup.service.imp;

import com.datarecord.webapp.datadictionary.service.imp.DataDictionaryServiceImp;
import com.datarecord.webapp.reportinggroup.bean.RcdJobUnitFld;
import com.datarecord.webapp.reportinggroup.bean.ReportingGroup;
import com.datarecord.webapp.reportinggroup.bean.rcdJobConfig;
import com.datarecord.webapp.reportinggroup.dao.ReportingGroupDao;
import com.datarecord.webapp.reportinggroup.service.ReportingGroupService;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("reportingGroupService")
public class ReportingGroupServiceImp  implements ReportingGroupService {

    private Logger logger = LoggerFactory.getLogger(DataDictionaryServiceImp.class);

    @Autowired
    private ReportingGroupDao reportingGroupDao;


    @Override
    public List<rcdJobConfig> leftrcdjobconfig() {
        return reportingGroupDao.leftrcdjobconfig();
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
    public void deletercdjobunitconfig(String job_unit_id) {
        reportingGroupDao.deletercdjobunitconfig(job_unit_id);
    }

    @Override
    public void rcdjobunitfld(String fld_id, String[] jobunitid) {
        for(int i=0;i<jobunitid.length;i++){
            reportingGroupDao.rcdjobunitfld(fld_id,jobunitid[i]);
        }
    }

    @Override
    public void rcdjobunitflddelete(String fld_id) {
        reportingGroupDao.rcdjobunitflddelete(fld_id);
    }

    @Override
    public List<RcdJobUnitFld> selectrcdjobunitfld(String job_unit_id) {
        return reportingGroupDao.selectrcdjobunitfld(job_unit_id);
    }
}
