package com.datarecord.webapp.process.dao;

import com.datarecord.webapp.process.entity.*;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;

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
            "rdf.fld_id ,"+
            "rdf.fld_name ,"+
            "rdf.fld_point ,"+
            "rdf.fld_data_type ,"+
            "rdf.fld_type ,"+
            "rdf.fld_is_null ,"+
            "rdf.is_actived "+
            "FROM rcd_job_unit_fld rjuf "+
            "left join rcd_dt_fld rdf on "+
            "rjuf.fld_id = rdf.fld_id "+
            "where rjuf.job_unit_id=#{unitId}")
    List<ReportFldConfig> getFldByUnitId(@Param("unitId") String unitId);


    @Update("DROP TABLE IF EXISTS rcd_report_data_job${jobId}")
    void dropJobDataTable(@Param("jobId") String jobId);

    @Update("CREATE TABLE rcd_report_data_job${jobId} ("+
            "id INT NOT NULL AUTO_INCREMENT,"+
            "report_id INT NULL,"+
            "unit_id INT NULL,"+
            "colum_id INT NULL,"+
            "fld_id INT NULL,"+
            "record_data VARCHAR(50) NULL,"+
            "PRIMARY KEY (id))")
    void makeJobDataTable(@Param("jobId") String jobId);

    @Insert("insert into rcd_report_data_job${jobId} " +
            "(report_id,unit_id,colum_id,fld_id,record_data) values " +
            "(#{rcdReportJobEntity.report_id}," +
            "#{rcdReportJobEntity.unit_id}," +
            "#{rcdReportJobEntity.colum_id}," +
            "#{rcdReportJobEntity.fld_id}," +
            "#{rcdReportJobEntity.record_data})")
    void createRcdReortJobData(@Param("rcdReportJobEntity") ReportJobData reportJobData, @Param("jobId") String jobId);

    @Update("update rcd_job_config set job_status = #{jobStatus} where job_id = #{jobId}")
    void changeRecordJobStatus(@Param("jobId") String jobId,@Param("jobStatus") int jobStatus);

    @Insert("insert into rcd_report_job " +
            "(job_id,record_user_id,record_origin_id,record_status) " +
            "values " +
            "(#{job_id},#{record_user_id},#{record_origin_id},#{record_status})")
    @Options(useGeneratedKeys = true, keyProperty = "report_id", keyColumn = "report_id")
    void createReportJobInfo(ReportJobInfo reportJobInfo);

    @Select("select report_id,job_id,record_user_id,record_origin_id,record_status from rcd_report_job where record_user_id=#{user_id}")
    Page<ReportJobInfo> pageJob(@Param("currPage") Integer currPage,
                                @Param("pageSize") Integer pageSize,
                                @Param("user_id") Integer user_id);

    @Select("select rjc.job_id,rjc.job_name,rjc.job_status,rjc.job_start_dt,rjc.job_end_dt  " +
            " from rcd_report_job rrj left join rcd_job_config rjc  " +
            "on rrj.job_id = rjc.job_id where report_id = #{reportId}")
    ReportJobInfo getReportJobInfoByReportId(String reportId);

    @Select("select id,report_id,unit_id,colum_id,fld_id,record_data from rcd_report_data_job${jobId} where report_id = #{reportId} and unit_id = #{unitId}")
    List<ReportJobData> getReportDataByUnitId(@Param("jobId") String jobId,@Param("reportId") String reportId,@Param("unitId") String unitId);
}
