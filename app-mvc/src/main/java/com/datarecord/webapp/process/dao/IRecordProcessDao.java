package com.datarecord.webapp.process.dao;

import com.datarecord.webapp.datadictionary.bean.DataDictionary;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.JobPerson;
import com.datarecord.webapp.process.entity.JobUnitConfig;
import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

@Repository
public interface IRecordProcessDao {

    @Select("select "+
            "job_id,"+
            "job_name,"+
            "job_status,"+
            "job_start_dt,"+
            "job_end_dt "+
            "from rcd_job_config where job_status!='3' and  job_id  = #{jobId}")
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
            "from rcd_job_unit_config where job_id = #{jobId} and job_unit_active = '1' ")
    @Results({
            @Result(property = "unitFlds",column = "job_unit_id",many = @Many(select="com.datarecord.webapp.process.dao.IRecordProcessDao.getFldByUnitId")),
            @Result(property = "job_unit_id",column = "job_unit_id")
    })
    List<JobUnitConfig> getJobUnitsByJobId(@Param("jobId") String jobId);

    @Select("SELECT " +
            "rdc.catg_name," +
            "rdc.catg_id,"+
            "rjuf.job_unit_id,"+
            "rdf.fld_id ,"+
            "rdf.fld_name ,"+
            "rdf.fld_point ,"+
            "rdf.fld_data_type ,"+
            "rdf.fld_type ,"+
            "rdf.fld_is_null ,"+
            "rdf.fld_range ,"+
            "rdf.fld_visible ,"+
            "rdf.fld_status ,"+
            "rdf.is_actived "+
            "FROM rcd_job_unit_fld rjuf "+
            "left join rcd_dt_fld rdf on "+
            "rjuf.fld_id = rdf.fld_id " +
            "left join rcd_dt_catg rdc on " +
            "rdf.catg_id = rdc.catg_id "+
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
    void changeRecordJobConfigStatus(@Param("jobId") String jobId, @Param("jobStatus") int jobStatus);

    @Update("update rcd_report_job set record_status = #{jobStatus} where report_id = #{report_id}")
    void changeRecordJobStatus(@Param("report_id") int jobId,@Param("jobStatus") int jobStatus);

    @Insert("insert into rcd_report_job " +
            "(job_id,record_user_id,record_origin_id,record_status) " +
            "values " +
            "(#{job_id},#{record_user_id},#{record_origin_id},#{record_status})")
    @Options(useGeneratedKeys = true, keyProperty = "report_id", keyColumn = "report_id")
    void createReportJobInfo(ReportJobInfo reportJobInfo);

    @Select("<script>" +
            "select rrj.report_id," +
            "rrj.job_id," +
            "rrj.record_user_id," +
            "u.user_name_cn as record_user_name," +
            "rrj.record_origin_id," +
            "so.origin_name as record_origin_name," +
            "rrj.record_status, " +
            "rjc.job_start_dt, " +
            "rjc.job_end_dt, " +
            "rjc.job_name " +
            "from rcd_report_job rrj " +
            "left join rcd_job_config rjc on " +
            "rrj.job_id = rjc.job_id  " +
            "left join user u on " +
            "rrj.record_user_id = u.user_id " +
            "left join user_origin_assign uoa on " +
            "u.user_id = uoa.user_id " +
            "left join sys_origin so on " +
            "uoa.origin_id = so.origin_id " +
            "where rjc.job_status!='3'  " +
            "<if test='user_id!=null and user_id!= \"\" '>" +
            " and rrj.record_user_id=#{user_id}" +
            "</if>" +
            "<if test='queryParams.reportStatus!=null and queryParams.reportStatus== \"0\" '>" +
            " and rrj.record_status=#{queryParams.reportStatus} and rjc.job_start_dt &lt; now() and rjc.job_end_dt &gt; now()" +
            "</if>" +
            "<if test='queryParams.reportStatus!=null and queryParams.reportStatus== \"1\" '>" +
            " and rrj.record_status=#{queryParams.reportStatus} " +
            "</if>" +
            "<if test='queryParams.reportStatus!=null and queryParams.reportStatus== \"9\" '>" +
            " and rrj.record_status=#{queryParams.reportStatus} " +
            "</if>" +
            "<if test='queryParams.reportStatus!=null and queryParams.reportStatus== \"7\" '>" +
            " and rrj.record_status=0  and rjc.job_start_dt &gt; now() " +
            "</if>" +
            "<if test='queryParams.reportStatus!=null and queryParams.reportStatus== \"8\" '>" +
            " and (rrj.record_status=0 or rrj.record_status=8  ) and rjc.job_end_dt &lt; now() " +
            "</if>" +
            "<if test='queryParams.reportStatus!=null and queryParams.reportStatus== \"10\" '>" +
            " and (rrj.record_status=0 or rrj.record_status=1  ) and rjc.job_end_dt &lt; now() " +
            "</if>" +
            "<if test='queryParams.reportName!=null'>" +
            " and rjc.job_name like concat('%',#{queryParams.reportName},'%') " +
            "</if> " +
            "order by rrj.report_id " +
            "</script>")
    Page<ReportJobInfo> pageJob(@Param("currPage") Integer currPage,
                                @Param("pageSize") Integer pageSize,
                                @Param("user_id") BigInteger user_id,
                                @Param("queryParams") Map<String,String> queryParams);

    @Select("<script>" +
            "select rrj.report_id," +
            "rrj.job_id," +
            "rrj.record_user_id," +
            "u.user_name_cn as record_user_name," +
            "rrj.record_origin_id," +
            "so.origin_name as record_origin_name," +
            "rrj.record_status, " +
            "rjc.job_start_dt, " +
            "rjc.job_end_dt, " +
            "rjc.job_name " +
            "from rcd_report_job rrj " +
            "left join rcd_job_config rjc on " +
            "rrj.job_id = rjc.job_id  " +
            "left join user u on " +
            "rrj.record_user_id = u.user_id " +
            "left join sys_origin so on " +
            "rrj.record_origin_id = so.origin_id " +
            "where rjc.job_status!='3' and rrj.report_id=#{reportId}" +
            "</script>")
    ReportJobInfo getReportJobInfoByReportId(String reportId);

    @Select("select id,report_id,unit_id,colum_id,fld_id,record_data from rcd_report_data_job${jobId} where report_id = #{reportId} and unit_id = #{unitId}")
    List<ReportJobData> getReportDataByUnitId(@Param("jobId") String jobId,@Param("reportId") String reportId,@Param("unitId") String unitId);

    @Select("SELECT  " +
            "rddc.dict_content_id, " +
            "rddc.dict_content_name, " +
            "rddc.dict_content_value, " +
            "rddc.dict_id FROM rcd_dt_fld_ct_assign rdfca left join  " +
            "rcd_dt_dict_content rddc on  " +
            "rdfca.dict_content_id = rddc.dict_content_id where  " +
            "rdfca.fld_id = #{fld_id}")
    List<DataDictionary> getDictcontent4Fld(Integer fld_id);

    @Delete("delete from rcd_report_data_job${job_id} where report_id=#{report_id} and unit_id =#{unit_id}")
    void deleteRecordDataByUnit(@Param("job_id") Integer job_id,@Param("report_id") Integer report_id,@Param("unit_id") Integer unitId);

    @Delete("delete from rcd_report_data_job${job_id} where report_id=#{report_id}")
    void deleteRecordDataByReport(@Param("job_id") Integer job_id,@Param("report_id") Integer report_id);

    @Delete("delete from rcd_report_data_job${job_id} where " +
            "report_id=#{rcdReportJobEntity.report_id} and " +
            "unit_id =#{rcdReportJobEntity.unit_id} and " +
            "colum_id=#{rcdReportJobEntity.colum_id} and " +
            "fld_id=#{rcdReportJobEntity.fld_id}")
    void deleteReportJobData(@Param("rcdReportJobEntity") ReportJobData delData,@Param("job_id") Integer job_id);

    @Update("update rcd_report_job set record_status=4 where job_id = #{job_id}")
    void softDelJobDatas(@Param("job_id") String job_id);

    @Update("update rcd_report_data_job${job_id} set " +
            "colum_id=#{newColumId},record_data=#{rcdReportJobEntity.record_data}" +
            " where " +
            "report_id=#{rcdReportJobEntity.report_id} and " +
            "unit_id =#{rcdReportJobEntity.unit_id} and " +
            "colum_id=#{rcdReportJobEntity.colum_id} and " +
            "fld_id=#{rcdReportJobEntity.fld_id}")
    void updateReportJobData(@Param("rcdReportJobEntity") ReportJobData updateData,
                             @Param("newColumId") Integer newColumId,
                             @Param("job_id") Integer jobId);

    @Update("update rcd_report_job set record_status = #{record_status} where report_id = #{report_id}")
    void updateReportStatus(@Param("report_id") String reportId,@Param("record_status") String value);

    @Delete("D")
    void dropJobDataByJobId(String jobId);

    @Delete("delete from rcd_job_person_group_log where job_id = #{jobId}")
    void delLogUserGroupHistory(String jobId);

    @Insert("insert into rcd_job_person_group_log (job_id,group_id,group_name,job_make_date)" +
            " values (#{job_id},#{group_id},#{group_name},#{job_make_date})")
    void logUserGroup(JobPersonGroupLog jobPersonGroupLog);

    @Select("select id,report_id,unit_id,colum_id,fld_id,record_data from rcd_report_data_job${jobId} " +
            "where report_id =  #{report_id} and unit_id = #{unit_id}")
    List<ReportJobData> getUnitDatas(@Param("job_id") String job_id,@Param("report_id") String report_id,@Param("unit_id") String unit_id);

    @Select("select distinct colum_id from rcd_report_data_job${job_id} " +
            "where report_id =  #{report_id} and unit_id = #{unit_id}")
    List<Integer> getUnitColums(@Param("job_id") String job_id,@Param("report_id") String report_id,@Param("unit_id") String unit_id);
}
