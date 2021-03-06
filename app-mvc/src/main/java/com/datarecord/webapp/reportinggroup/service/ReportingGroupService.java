package com.datarecord.webapp.reportinggroup.service;

import com.datarecord.webapp.reportinggroup.bean.RcdJobUnitFld;
import com.datarecord.webapp.reportinggroup.bean.ReportingGroup;
import com.datarecord.webapp.reportinggroup.bean.rcdJobConfig;
import com.webapp.support.page.PageResult;

import java.math.BigInteger;
import java.util.List;

public interface ReportingGroupService {
    List<rcdJobConfig> leftrcdjobconfig(BigInteger user_id);

    PageResult rcdjobunitconfiglist(int currPage, int pageSize, String job_id);

    void deletercdjobunitconfig(String job_unit_id);

    void rcdjobunitfld(String fld_id, String jobunitid);


    void insertrcdjobunitconfig(ReportingGroup reportingGroup);

    void updatercdjobunitconfig(ReportingGroup reportingGroup);

    ReportingGroup selectrcdjobunitconfigByjobunitid(String job_unit_id);

    List<RcdJobUnitFld> selectrcdjobunitfld(BigInteger user_id, String job_unit_id);

    List<RcdJobUnitFld> selectrcdjobunitfldWu(String job_unit_id);
}
