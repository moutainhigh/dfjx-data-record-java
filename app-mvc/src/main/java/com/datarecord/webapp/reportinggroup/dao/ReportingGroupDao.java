package com.datarecord.webapp.reportinggroup.dao;

import com.datarecord.webapp.reportinggroup.bean.RcdJobUnitFld;
import com.datarecord.webapp.reportinggroup.bean.ReportingGroup;
import com.datarecord.webapp.reportinggroup.bean.rcdJobConfig;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReportingGroupDao {

    @Select("SELECT job_id,job_name FROM rcd_job_config")
    List<rcdJobConfig> leftrcdjobconfig();

    @Select("SELECT job_unit_id,job_unit_name,job_unit_active FROM rcd_job_unit_config where job_id =#{job_id}")
    Page<ReportingGroup> rcdjobunitconfiglist(@Param("currPage") int currPage, @Param("pageSize") int pageSize, @Param("job_id") String job_id);

    @Delete("DELETE FROM rcd_job_unit_config WHERE  job_unit_id =#{job_unit_id}")
    void deletercdjobunitconfig(@Param("job_unit_id") String job_unit_id);

    @Insert("INSERT  INTO rcd_job_unit_fld(job_unit_id,fld_id) VALUES(#{jobunitid},#{fldid})")
    void rcdjobunitfld(@Param("fldid") String fldid, @Param("jobunitid") String jobunitid);

    @Delete("DELETE FROM rcd_job_unit_fld WHERE  job_unit_id =#{jobunitid}")
    void rcdjobunitflddelete(@Param("jobunitid") String jobunitid);

    @Select("SELECT a.fld_id,b.fld_name FROM rcd_job_unit_fld  a left join  rcd_dt_fld  b   on  a.fld_id = b.fld_id    where a.job_unit_id = #{job_unit_id}")
    List<RcdJobUnitFld> selectrcdjobunitfld(@Param("job_unit_id") String job_unit_id);

    @Insert("INSERT  INTO rcd_job_unit_config(job_unit_name,job_id,job_unit_active,job_unit_type) VALUES(#{job_unit_name},#{job_id},#{job_unit_active},#{job_unit_type})")
    void insertrcdjobunitconfig(@Param("job_id") String job_id, @Param("job_unit_name") String job_unit_name, @Param("job_unit_active") String job_unit_active,@Param("job_unit_type") String job_unit_type);

    @Delete("DELETE FROM rcd_job_unit_fld WHERE  job_unit_id =#{job_unit_id}")
    void deletercdjobunitfld(@Param("job_unit_id")String job_unit_id);
}
