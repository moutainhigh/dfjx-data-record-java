package com.datarecord.webapp.process.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface IReportSumDao {
    @Insert("insert into rcd_reportfile_log " +
            "(report_id,job_id,start_time,log_status,log_user,log_type)" +
            " values " +
            "(#{report_id},#{job_id},#{start_time},#{log_status},#{log_user},#{log_type})")
    @Options(useGeneratedKeys = true, keyProperty = "log_id", keyColumn = "log_id")
    void recordFileLog(ReportFileLog reportFileLog);

    @Update("update rcd_reportfile_log set " +
            "log_status=#{log_status}," +
            "end_time=#{end_time}," +
            "file_path=#{file_path}," +
            "comment=#{comment} " +
            "where log_id = #{log_id}")
    void updateRecordFileLog(ReportFileLog reportFileLog);


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
            "from rcd_reportfile_log where log_id = #{logId}")
    ReportFileLog getReportFile(String logId);

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
    List<ReportFileLog> getSumJobFileList(@Param("jobId") String jobId,@Param("userId") BigInteger userId);
}
