package com.datarecord.webapp.process.dao;

import com.datarecord.webapp.process.entity.*;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface IRecordProcessFlowDao {

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
            "where " +
            " rrj.record_status!='4' and  rrj.record_origin_id in " +
            "<foreach item='item' index='index' collection='originIds' open='(' separator=',' close=')'> " +
            " #{item.origin_id} " +
            "</foreach>" +
            "<if test='reportStatus!=null and reportStatus== \"0\" '>" +
            " and rrj.record_status=#{reportStatus} and rjc.job_start_dt &lt; now() and rjc.job_end_dt &gt; now()" +
            "</if>" +
            "<if test='reportStatus!=null and reportStatus== \"1\" '>" +
            " and rrj.record_status=#{reportStatus} " +
            "</if>" +
            "<if test='reportStatus!=null and reportStatus== \"9\" '>" +
            " and rrj.record_status=#{reportStatus} " +
            "</if>" +
            "<if test='reportStatus!=null and reportStatus== \"7\" '>" +
            " and rrj.record_status=0 and rjc.job_start_dt &gt; now()" +
            "</if>" +
            "<if test='reportStatus!=null and reportStatus== \"8\" '>" +
            "  and (rrj.record_status=0 or rrj.record_status=8  ) and rjc.job_end_dt &lt; now()  " +
            "</if>" +
            "<if test='reportName!=null'>" +
            " and rjc.job_name like concat('%',#{reportName},'%')"+
            "</if>"+
            "</script>")
    Page<ReportJobInfo> pageReviewDatas(
            @Param("currPage") Integer currPage,
            @Param("pageSize") Integer pageSize,
            @Param("originIds") List originIds,
            @Param("reportStatus") String reportStatus,
            @Param("reportName") String reportName);

    @Update("update rcd_dt_fld set fld_status = #{reviewStatus} where fld_id = #{fld_id}")
    void reviewFld(@Param("fld_id") String fldId,@Param("reviewStatus") String reviewStatus);

    @Select("<script>" +
            "select "+
            "rjc.job_id,"+
            "rjc.job_name,"+
            "rjc.job_status,"+
            "rjc.job_start_dt,"+
            "rjc.job_end_dt ,"+
            "rjc.job_creater ,"+
            "rjc.job_creater_origin ,"+
            "su.user_name as job_creater_name,"+
            "so.origin_name as job_creater_origin_name "+
            "from rcd_job_config rjc " +
            "left join user su on " +
            "rjc.job_creater = su.user_id " +
            "left join sys_origin so on " +
            "rjc.job_creater_origin = so.origin_id " +
            " where rjc.job_status!='3' and " +
            "rjc.job_creater_origin in " +
            "<foreach item='item' index='index' collection='originIds' open='(' separator=',' close=')'> " +
            " #{item.origin_id}" +
            "</foreach> " +
            "<if test='queryParams.jobName!=null'>" +
            " and rjc.job_name like  concat('%',#{queryParams.jobName},'%')" +
            "</if>" +
            "<if test='queryParams.jobStatus!=null'>" +
            " and rjc.job_status = #{queryParams.jobStatus}" +
            "</if>" +
            "</script>")
    Page<JobConfig> pageReviewJobs(
            @Param("currPage") String currPage,
            @Param("pageSize") String pageSize,
            @Param("originIds") List<Origin> originIds,
            @Param("queryParams") Map<String, String> queryParams);

    @Select("<script>" +
            "SELECT " +
            "rdf.catg_id," +
            " rdc.catg_name," +
            " rdf.fld_id," +
            " rdf.fld_name," +
            " rdf.fld_point," +
            " rdf.fld_data_type," +
            " rdf.fld_type," +
            " rdf.fld_is_null," +
            " rdf.is_actived," +
            " rdf.fld_range," +
            " rdf.fld_visible," +
            " rdf.fld_status," +
            " rdf.fld_creater," +
            " su.user_name_cn as fld_creater_name," +
            " rdf.fld_creater_origin," +
            " so.origin_name as fld_creater_origin_name " +
            "FROM rcd_dt_fld rdf left join rcd_dt_catg rdc on " +
            "rdf.catg_id = rdc.catg_id left join user su on " +
            "rdf.fld_creater = su.user_id left join sys_origin so on " +
            "rdf.fld_creater_origin = so.origin_id " +
            "where rdf.fld_creater_origin in " +
            "<foreach item='item' index='index' collection='originIds' open='(' separator=',' close=')'>" +
            "  #{item.origin_id}" +
            "</foreach> " +
            "<if test='queryParams.fldName!=null'>" +
            " and rdf.fld_name like  concat('%',#{queryParams.fldName},'%')" +
            "</if>" +
            "<if test='queryParams.fldStatus!=null'>" +
            " and rdf.fld_status = #{queryParams.fldStatus}" +
            "</if>" +
            "</script>")
    Page<ReportFldConfig> pageReviewFlds(
            @Param("currPage") String currPage,
            @Param("pageSize") String pageSize,
            @Param("originIds") List<Origin> originIds,
            @Param("queryParams") Map<String, String> queryParams);

    @Update("update rcd_job_config set job_status = #{status} where job_id = #{jobId}")
    void changeJobConfig(@Param("jobId") String jobId,@Param("status") String status);

    @Insert("insert into rcd_job_flowlog " +
            "(job_id,job_flow_status,job_flow_comment,job_flow_user,job_flow_date) " +
            "values " +
            "(#{job_id},#{job_flow_status},#{job_flow_comment},#{job_flow_user},#{job_flow_date})")
    void recordFlowLog(JobFlowLog jobFlowLog);

    @Select("select " +
            "rjf.id," +
            "rjf.job_id," +
            "rjf.job_flow_status," +
            "rjf.job_flow_comment," +
            "rjf.job_flow_date," +
            "rjf.job_flow_user, " +
            "u.user_name_cn as job_flow_user_name " +
            "from rcd_job_flowlog rjf " +
            "left join user u on " +
            "rjf.job_flow_user = u.user_id where rjf.job_id = #{jobId}")
    List<JobFlowLog> listJobFlowLogs(String jobId);
}


