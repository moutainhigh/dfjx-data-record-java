package com.datarecord.webapp.process.dao;

import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobPerson;
import com.datarecord.webapp.process.entity.JobUnitConfig;
import com.datarecord.webapp.process.entity.RcdReportJobEntity;
import dataindex.bean.RcdDt;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.List;

public interface IRecordProcessDao {

    @Select("select "+
            "job_id,"+
            "job_name,"+
            "job_status,"+
            "job_start_dt,"+
            "job_end_dt "+
            "from rcd_job_config where job_id  = #{jobId}")
    @Results({
            @Result(property = "jobUnits",column = "job_id",many = @Many(select="com.datarecord.webapp.process.dao.IRecordProcessDao.getJobUnitsByJobId")),
            @Result(property = "jobPersons",column = "job_id",many = @Many(select="com.datarecord.webapp.process.dao.IRecordProcessDao.getJobPersonsByJobId")),
            @Result(property = "job_id",column = "job_id")
    })
    JobConfig getJobConfigByJobId(@Param("jobId") String jobId);


    @Select("SELECT  " +
            "rjpa.job_id, " +
            "u.user_id, " +
            "u.user_name, " +
            "u.user_name_cn , " +
            "so.origin_id, " +
            "so.origin_name " +
            "FROM rcd_job_person_assign rjpa   " +
            "left join user u " +
            "on rjpa.user_id = u.user_id " +
            "left join user_origin_assign uoa " +
            "on u.user_id = uoa.user_id " +
            "left join sys_origin so  " +
            "on uoa.origin_id = so.origin_id " +
            "where rjpa.job_id = #{jobId}")
    List<JobPerson> getJobPersonsByJobId(@Param("jobId") String jobId);

    @Select("select "+
            "job_unit_id,"+
            "job_unit_name,"+
            "job_id,"+
            "job_unit_active,"+
            "job_unit_type "+
            "from rcd_job_unit_config where job_id = #{jobId}")
    @Results({
            @Result(property = "unitFlds",column = "job_unit_id",many = @Many(select="com.datarecord.webapp.process.dao.IRecordProcessDao.getFldByUnitId")),
            @Result(property = "job_unit_id",column = "job_unit_id")
    })
    List<JobUnitConfig> getJobUnitsByJobId(@Param("jobId") String jobId);



    @Select("SELECT "+
            "rjuf.job_unit_id,"+
            "rdf.* "+
            "FROM rcd_job_unit_fld rjuf "+
            "left join rcd_dt_fld rdf on "+
            "rjuf.fld_id = rdf.fld_id "+
            "where rjuf.job_unit_id=#{unitId}")
    List<RcdDt> getFldByUnitId(@Param("unitId") String unitId);


    @Update("DROP TABLE IF EXISTS rcd_report_job${jobId}")
    void dropJobDataTable(@Param("jobId") String jobId);

    @Update("CREATE TABLE rcd_report_job${jobId} ("+
            "id INT NOT NULL AUTO_INCREMENT,"+
            "unit_id INT NULL,"+
            "colum_id INT NULL,"+
            "fld_id INT NULL,"+
            "record_data VARCHAR(50) NULL,"+
            "record_origin_id INT NULL,"+
            "record_user_id INT NULL,"+
            "record_status INT NULL,"+
            "PRIMARY KEY (id))")
    void makeJobDataTable(@Param("jobId") String jobId);

    @Insert("insert into rcd_report_job${jobId} " +
            "(unit_id,colum_id,fld_id,record_data,record_origin_id,record_user_id) values " +
            "(#{rcdReportJobEntity.unit_id}," +
            "#{rcdReportJobEntity.colum_id}," +
            "#{rcdReportJobEntity.fld_id}," +
            "#{rcdReportJobEntity.record_data}," +
            "#{rcdReportJobEntity.record_origin_id}," +
            "#{rcdReportJobEntity.record_user_id})")
    void createRcdReortJobData(@Param("rcdReportJobEntity") RcdReportJobEntity rcdReportJobEntity,@Param("jobId") String jobId);

    @Update("update rcd_job_config set job_status = #{jobStatus} where job_id = #{jobId}")
    void changeRecordJobStatus(@Param("jobId") String jobId,@Param("jobStatus") int jobStatus);
}
