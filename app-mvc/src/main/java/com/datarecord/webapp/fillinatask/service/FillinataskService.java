package com.datarecord.webapp.fillinatask.service;

import com.datarecord.webapp.fillinatask.bean.Fillinatask;
import com.datarecord.webapp.fillinatask.bean.RcdJobPersonAssign;
import com.datarecord.webapp.fillinatask.bean.RcdJobUnitConfig;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;

import java.util.List;

public interface FillinataskService {

    PageResult rcdjobconfiglist(int currPage, int pageSize, String job_name, String job_status);

    void insertrcdjobconfig(String job_name, String job_start_dt, String job_end_dt);

    void insertrcdjobpersonassign(String job_id, String userid);

    void deletercdjobpersonassign(String job_id);

    void updatercdjobconfig(String job_id, String job_name, String job_start_dt, String job_end_dt);

    List<RcdJobUnitConfig> selectRcdJobUnitConfig(String job_id);

    List<RcdJobUnitConfig> selectRcdJobUnitConfigyi(String job_id);

    void updateRcdJobUnitConfigyi(String jobunitid, String job_id);

    void updateRcdJobUnitConfigsuo(String job_id);

    List<RcdJobPersonAssign> huixianrcdjobpersonassign(String job_id);

    void deletercdjobconfig(String job_id);

    void deleteRcdJobUnitConfigsuo(String job_id);

    List<Fillinatask> selectrcdjobconfigjobid(String job_id);

    void deletercdjobpersonassignbyuseridandjobid(String job_id, String user_id);
}
