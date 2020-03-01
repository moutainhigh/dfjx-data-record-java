package com.datarecord.webapp.reportinggroup.dao;

import com.datarecord.webapp.reportinggroup.bean.*;
import com.datarecord.webapp.utils.EntityTree;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReportingGroupDao {

    @Select("<script>SELECT job_id,job_name,job_status FROM rcd_job_config  where   job_creater_origin IN " +
            "  <foreach item=\"item\" index=\"index\" collection=\"originIds\" open=\"(\" separator=\",\" close=\")\">" +
            "  #{item}" +
            "  </foreach>"+
            "  </script>")
    List<rcdJobConfig> leftrcdjobconfig(@Param("originIds") List<Integer> originIds);

    @Select("SELECT job_unit_id,job_unit_name,job_unit_active FROM rcd_job_unit_config where job_id =#{job_id}")
    Page<ReportingGroup> rcdjobunitconfiglist(@Param("currPage") int currPage, @Param("pageSize") int pageSize, @Param("job_id") String job_id);

    @Delete("DELETE FROM rcd_job_unit_config WHERE  job_unit_id =#{job_unit_id}")
    void deletercdjobunitconfig(@Param("job_unit_id") String job_unit_id);

    @Insert("INSERT  INTO rcd_job_unit_fld(job_unit_id,fld_id) VALUES(#{jobunitid},#{fldid})")
    void rcdjobunitfld(@Param("fldid") String fldid, @Param("jobunitid") String jobunitid);

    @Delete("DELETE FROM rcd_job_unit_fld WHERE  job_unit_id =#{jobunitid}")
    void rcdjobunitflddelete(@Param("jobunitid") String jobunitid);

    @Select("<script>SELECT " +
            " a.fld_id, " +
            " b.fld_name " +
            " FROM " +
            " rcd_job_unit_fld a " +
            " LEFT JOIN rcd_dt_fld b ON a.fld_id = b.fld_id " +
            " WHERE " +
            " a.job_unit_id = #{job_unit_id} " +
            " AND b.fld_creater  = #{user_id} " +
            " </script>")
    List<RcdJobUnitFld> selectrcdjobunitfld(@Param("user_id")int  user_id,@Param("job_unit_id") String job_unit_id);

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

    @Insert("insert into rcd_job_unit_flow (job_id,unit_id,edit_after_sub,edit_reviewer) values " +
            "(#{job_id},#{unit_id},#{edit_after_sub},#{edit_reviewer})")
    void saveUnitFlow(RcdJobUnitFlow rcdJobUnitFlow);

    @Update("update rcd_job_unit_flow set edit_after_sub=#{edit_after_sub} where job_id=#{job_id} and unit_id=#{unit_id}")
    void editUnitFlow(RcdJobUnitFlow rcdJobUnitFlow);

    @Select("select job_id,unit_id,edit_after_sub,edit_reviewer from rcd_job_unit_flow " +
            "where job_id = #{job_id} and unit_id = #{job_unit_id} ")
    RcdJobUnitFlow getUnitFLow(@Param("job_id") Integer job_id,@Param("job_unit_id") String job_unit_id);

}
