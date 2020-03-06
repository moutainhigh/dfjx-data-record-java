package com.datarecord.webapp.reportinggroup.dao;

import com.datarecord.webapp.reportinggroup.bean.RcdJobUnitFld;
import com.datarecord.webapp.reportinggroup.bean.ReportGroupInterval;
import com.datarecord.webapp.reportinggroup.bean.ReportingGroup;
import com.datarecord.webapp.reportinggroup.bean.rcdJobConfig;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportingGroupDao {

    @Select("SELECT job_id,job_name,job_status FROM rcd_job_config")
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

    @Insert("INSERT  INTO rcd_job_unit_config(job_unit_name,job_id,job_unit_active,job_unit_type,job_unit_cycle) VALUES " +
            "(#{job_unit_name},#{job_id},#{job_unit_active},#{job_unit_type},#{job_unit_cycle})")
    @Options(useGeneratedKeys = true, keyProperty = "job_unit_id", keyColumn = "job_unit_id")
    void insertrcdjobunitconfig( ReportingGroup reportingGroup);

    @Delete("DELETE FROM rcd_job_unit_fld WHERE  job_unit_id =#{job_unit_id}")
    void deletercdjobunitfld(@Param("job_unit_id")String job_unit_id);

    @Update("UPDATE rcd_job_unit_config SET " +
            "job_unit_name=#{job_unit_name},job_unit_active =#{job_unit_active},job_unit_type=#{job_unit_type} " +
            " WHERE  job_unit_id = #{job_unit_id} ")
    void updatercdjobunitconfig(ReportingGroup reportingGroup);


    @Select("SELECT * FROM rcd_job_unit_config where job_unit_id =#{job_unit_id}")
    ReportingGroup selectrcdjobunitconfigByjobunitid(@Param("job_unit_id")String job_unit_id);

    @Insert("insert into rcd_job_unit_interval (job_id,job_unit_id,job_unit_start,job_unit_end) values " +
            "(#{reportingGroupInterval.job_id},#{reportingGroupInterval.job_unit_id},#{reportingGroupInterval.job_unit_start},#{reportingGroupInterval.job_unit_end})")
    void saveReportingInterval(@Param("reportingGroupInterval") ReportGroupInterval reportingGroupInterval);

    @Delete("<script>delete from rcd_job_unit_interval where " +
            "job_unit_id=#{job_unit_id}" +
            "<if test='job_id!=null'>" +
            " and job_id = #{job_id}" +
            "</if>  " +
            "</script>")
    void deleteReportingInterval(@Param("job_id") Integer job_id,@Param("job_unit_id") Integer job_unit_id);

    @Select("select job_id,job_unit_id,job_unit_start,job_unit_end from rcd_job_unit_interval where job_id = #{job_id} and job_unit_id=#{job_unit_id}")
    List<ReportGroupInterval> getReportGroupInterval(@Param("job_id") Integer job_id,@Param("job_unit_id") Integer job_unit_id);
}
