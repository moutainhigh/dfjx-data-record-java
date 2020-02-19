package com.datarecord.webapp.reportinggroup.service;

import com.datarecord.webapp.reportinggroup.bean.RcdJobUnitFld;
import com.datarecord.webapp.reportinggroup.bean.rcdJobConfig;
import com.webapp.support.page.PageResult;

import java.util.List;

public interface ReportingGroupService {
    List<rcdJobConfig> leftrcdjobconfig();

    PageResult rcdjobunitconfiglist(int currPage, int pageSize, String job_id);

    void deletercdjobunitconfig(String job_unit_id);

    void rcdjobunitfld(String fld_id, String jobunitid);

    void rcdjobunitflddelete(String jobunitid);

    List<RcdJobUnitFld> selectrcdjobunitfld(String job_unit_id);

    void insertrcdjobunitconfig(String job_id, String job_unit_name, String job_unit_active,String job_unit_type);
}
