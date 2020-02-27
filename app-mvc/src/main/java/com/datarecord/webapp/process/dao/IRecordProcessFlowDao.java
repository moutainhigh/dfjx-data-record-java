package com.datarecord.webapp.process.dao;

import com.datarecord.webapp.process.entity.ReportJobData;
import com.datarecord.webapp.process.entity.ReportJobInfo;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

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
            "where rrj.record_origin_id in " +
            "<foreach item='item' index='index' collection='originIds' open='(' separator=',' close=')'> " +
            " #{item.origin_id} " +
            "</foreach>" +
            "<if test='reportStatus!=null'>" +
            " and rrj.record_status = #{reportStatus}"+
            "</if>"+
            "</script>")
    Page<ReportJobInfo> pageReviewDatas(
            @Param("currPage") Integer currPage,
            @Param("pageSize") Integer pageSize,
            @Param("originIds") List originIds,
            @Param("reportStatus") String reportStatus);

}


