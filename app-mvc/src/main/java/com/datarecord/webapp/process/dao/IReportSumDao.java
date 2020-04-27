package com.datarecord.webapp.process.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface IReportSumDao {
    @Insert("insert into rcd_reportfile_log " +
            "(report_id,job_id,start_time,log_status,log_user)" +
            " values " +
            "(#{report_id},#{job_id},#{start_time},#{log_status},#{log_user})")
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
}
