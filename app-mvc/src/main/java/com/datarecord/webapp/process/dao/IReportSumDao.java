package com.datarecord.webapp.process.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface IReportSumDao {

    @Select("select " +
            "log_id," +
            "report_id," +
            "job_id," +
            "start_time," +
            "end_time," +
            "log_status," +
            "comment," +
            "file_path," +
            "log_user " +
            "from rcd_reportfile_log where job_id = #{jobId} and log_user = #{userId} and log_type=2")
    List<ReportFileLog> getSumJobFileList(@Param("jobId") String jobId, @Param("userId") BigInteger userId);
}
