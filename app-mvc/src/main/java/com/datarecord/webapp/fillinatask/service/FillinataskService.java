package com.datarecord.webapp.fillinatask.service;

import com.datarecord.webapp.fillinatask.bean.RcdJobUnitConfig;
import com.webapp.support.page.PageResult;

import java.util.List;

public interface FillinataskService {

    PageResult rcdjobconfiglist(int currPage, int pageSize, String job_name, String job_status);

    void insertrcdjobconfig(String job_name, String job_start_dt, String job_end_dt);

    void insertrcdjobpersonassign(String job_id, String[] userid);

    void deletercdjobpersonassign(String job_id);

    void updatercdjobconfig(String job_id, String job_name, String job_start_dt, String job_end_dt);

    List<RcdJobUnitConfig> selectRcdJobUnitConfig(String job_id);

    List<RcdJobUnitConfig> selectRcdJobUnitConfigyi(String job_id);

    void updateRcdJobUnitConfigyi(String[] jobunitid, String job_id);

    void updateRcdJobUnitConfigsuo(String job_id);
}
