package com.datarecord.webapp.fillinatask.dao;

import com.datarecord.webapp.fillinatask.bean.*;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobUnitConfig;
import com.datarecord.webapp.process.entity.ReportFldConfig;
import com.datarecord.webapp.reportinggroup.bean.ReportGroupInterval;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IJobConfigDao {

    @Select("<script> select * from  rcd_job_config  where  1=1  " +
            "<if test = 'job_name != null' > AND job_name like concat('%',#{job_name},'%') </if> " +
            "<if test = 'job_status != null' > AND job_status =#{job_status} </if> " +
            "<if test = 'user_id != null' > AND job_creater =#{user_id} </if> " +
            " order by job_id </script> ")
    Page<Fillinatask> rcdjobconfiglist(@Param("currPage") int currPage,
                                       @Param("pageSize") int pageSize,
                                       @Param("job_name") String job_name,
                                       @Param("job_status") String job_status,
                                       @Param("user_id") String user_id);


    @Insert("insert into rcd_job_config " +
            "(job_name,job_start_dt,job_end_dt,job_creater,job_creater_origin,job_cycle) " +
            "values " +
            "(#{job_name},#{job_start_dt},#{job_end_dt},#{job_creater},#{job_creater_origin},#{job_cycle})")
    @Options(useGeneratedKeys = true, keyProperty = "job_id", keyColumn = "job_id")
    void saveJobConfig(JobConfig jobConfig);

    @Insert("insert into rcd_job_interval " +
            "(job_id,job_interval_start,job_interval_end) " +
            "values " +
            "(#{job_id},#{job_interval_start},#{job_interval_end})")
    void saveJobInterval(JobInteval jobInterval);


    @Insert("INSERT INTO rcd_job_person_assign(user_id,job_id)  VALUES(#{user_id},#{job_id})")
    void saveJobPersonAssign(@Param("job_id") String job_id, @Param("user_id") String user_id);

    @Delete("delete from rcd_job_person_assign where  job_id = #{job_id}")
    void delJobPersonAssign(@Param("job_id") String job_id);


    @Update("update rcd_job_config set job_name = #{job_name}, job_start_dt= #{job_start_dt}, job_end_dt =#{job_end_dt}  where  job_id = #{job_id}")
    void updatercdjobconfig(JobConfig jobConfig);

    @Select("select  job_unit_id,job_unit_name from rcd_job_unit_config where job_id = #{job_id} and job_unit_active = 0")
    List<RcdJobUnitConfig> selectRcdJobUnitConfig(@Param("job_id") String job_id);

    @Select("select  job_unit_id,job_unit_name from rcd_job_unit_config where job_id = #{job_id} and  job_unit_active = 1")
    List<RcdJobUnitConfig> selectRcdJobUnitConfigyi(@Param("job_id") String job_id);

    @Update("update  rcd_job_unit_config set  job_unit_active = 1 where job_unit_id = #{job_unit_id} and job_id = #{job_id} ")
    void updateRcdJobUnitConfigyi(@Param("job_unit_id") String job_unit_id, @Param("job_id") String job_id);

    @Update("update  rcd_job_unit_config set  job_unit_active = 0 where  job_id = #{job_id} ")
    void updateRcdJobUnitConfigsuo(@Param("job_id") String job_id);

    @Select("select u.user_id,u.user_name_cn,so.origin_id from rcd_job_person_assign rjpa left join user u " +
            " on rjpa.user_id = u.user_id left join user_origin_assign uoa  " +
            " on u.user_id = uoa.user_id left join sys_origin so " +
            " on uoa.origin_id = so.origin_id  where rjpa.job_id = #{job_id} ")
    List<RcdJobPersonAssign> huixianrcdjobpersonassign(@Param("job_id") String job_id);

    @Delete("delete from rcd_job_config where job_id = #{job_id} ")
    void deletercdjobconfig(@Param("job_id") String job_id);


    /*任务软删除*/
    @Update("update rcd_job_config set job_status= 3   where job_id = #{job_id} ")
    void updsoftDelJobConfig(@Param("job_id") String job_id);

    /*填报人维护软删除*/
    @Update("update  rcd_job_person_assign  set   assign_status = 1   where  job_id = #{job_id}")
    void updateRcdjobpersonassign(@Param("job_id") String job_id);

    /*任务关连填报组软删除*/
    @Update("update  rcd_job_unit_config  set  job_unit_active = 0     where  job_id = #{job_id}")
    void updateByidRcdJobUnitConfigsuo(@Param("job_id") String job_id);

    /*已下发的填报状态修改*/
    @Update("update  rcd_report_job  set   report_status =  4     where job_id = #{job_id} ")
    void updateRcdReportJob(@Param("job_id") String job_id);

    @Delete("delete from rcd_job_unit_config where job_id = #{job_id} ")
    void deleteRcdJobUnitConfigsuo(@Param("job_id") String job_id);

    @Select("select * from  rcd_job_config  where  job_id = #{job_id}")
    JobConfig selectrcdjobconfigjobid(@Param("job_id") String job_id);

    @Delete("delete from rcd_job_person_assign  WHERE job_id = #{job_id}  AND  user_id =#{user_id}")
    void deletercdjobpersonassignbyuseridandjobid(@Param("job_id") String job_id, @Param("user_id") String user_id);


    @Select("select  job_name from rcd_job_config  where job_id = #{jobid} group by job_name")
    String selectrcdjobconfig(@Param("jobid") Integer jobid);


    @Select("select job_unit_name  from rcd_job_unit_config  where  job_unit_id = #{unitId} group by job_unit_name")
    String selectrcdjobunitconfig(@Param("unitId") String unitId);


    @Select("<script>SELECT a.record_data,b.fld_name  from  rcd_report_data_job${jobid}  a  LEFT JOIN   rcd_dt_fld  b  on a.fld_id = b.fld_id   where  a.report_id = #{reportid} and  a.unit_id = #{unitId}  and  a.fld_id  in" +
            "<foreach item='item' index='index' collection='fldids' open='(' separator=',' close=')'> " +
            " #{item} " +
            "</foreach>" +
            "    </script> ")
    List<Lieming> selectrcdreportdatajob(@Param("jobid") int jobid, @Param("reportid") int reportid, @Param("unitId") String unitId, @Param("fldids") List fldids);


    @Update("update  rcd_job_config set  job_status = 6  where  job_id =#{job_id}")
    void approveJobConfig(@Param("job_id") String job_id);


    @Select("SELECT job_unit_id,job_unit_name from  rcd_job_unit_config  where  job_id = #{job_id}")
    List<JobUnitConfig> taskDetailsjobUnitConfig(@Param("job_id") String job_id);


    @Select("select d.fld_id,d.fld_name from rcd_job_config a " +
            "INNER JOIN rcd_job_unit_config b  on a.job_id = b.job_id  " +
            "INNER JOIN rcd_job_unit_fld   c  on  b.job_unit_id = c.job_unit_id  " +
            "INNER JOIN rcd_dt_fld  d on  c.fld_id = d.fld_id    where a.job_id  = #{job_id} ")
    List<ReportFldConfig> taskDetailsreportFldConfig(@Param("job_id") String job_id);

    @Select("select job_id,job_interval_start,job_interval_end from rcd_job_interval where job_id = #{job_id}")
    List<JobInteval> getJobIntevals(@Param("job_id") String job_id);

    @Delete("delete from rcd_job_interval where job_id = #{job_id} ")
    void removeIntervals(Integer job_id);

    @Select("select job_id,job_interval_start,job_interval_end from rcd_job_interval ")
    List<JobInteval> allJobIntevals();
}
