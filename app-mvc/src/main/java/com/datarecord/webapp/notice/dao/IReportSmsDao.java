package com.datarecord.webapp.notice.dao;

import com.datarecord.webapp.notice.entity.Holiday;
import com.datarecord.webapp.notice.entity.ReportSmsConfig;
import com.datarecord.webapp.process.entity.JobConfig;
import com.github.pagehelper.Page;
import com.workbench.auth.user.entity.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IReportSmsDao {

    @Select("select * from china_holidays")
    List<Holiday> getHolidays();

    @Select("select " +
            "id," +
            "config_name," +
            "report_defined_id," +
            "sms_template_id," +
            "send_date," +
            "send_time," +
            "pre_days," +
            "cross_holiday," +
            "send_status" +
            " from report_sms_config where config_status='0'")
    List<ReportSmsConfig> getTodayReportSmsConfigs(String currDate);

    @Insert("insert into report_sms_config (" +
            "id," +
            "config_name," +
            "report_defined_id," +
            "sms_template_id," +
            "send_date," +
            "send_time," +
            "distance_type," +
            "distance_days," +
            "cross_holiday," +
            "send_status) values (" +
            "#{id}," +
            "#{config_name}," +
            "#{report_defined_id}," +
            "#{sms_template_id}," +
            "#{send_date}," +
            "#{send_time}," +
            "#{distance_type}," +
            "#{distance_days}," +
            "#{cross_holiday}," +
            "'0')")
    void saveSmsJob(ReportSmsConfig reportSmsConfig);

    @Update("update report_sms_config set " +
            "config_name=#{config_name}," +
            "report_defined_id=#{report_defined_id}," +
            "sms_template_id=#{sms_template_id}," +
            "send_date=#{send_date}," +
            "send_time=#{send_time}," +
            "distance_type=#{distance_type}," +
            "distance_days=#{distance_days}," +
            "cross_holiday=#{cross_holiday}," +
            "send_status=#{send_status} where id=#{id}")
    void updateSmsJob(ReportSmsConfig reportSmsConfig);

    @Update("update report_sms_config set send_status = #{status} where id = #{configId}")
    void changeJobStatus(@Param("configId") Integer configId,@Param("status") String status);

    @Select("select so.origin_id,so.origin_name,u.* from report_defined_origin_assign rdoa,sys_origin so ,user_origin_assign uoa ," +
            "user u  where rdoa.defined_id='26' and rdoa.origin_id = so.origin_id and " +
            "so.origin_id = uoa.origin_id and uoa.user_id = u.user_id")
    List<User> getReportUsers(String reportDefinedId);

    @Select("select rc.report_origin,count(0) report_count from report_customer rc where rc.report_defined_id = #{reportDefinedId} " +
            "and report_end_date >= #{sendDate} and report_status='0' group by rc.report_origin")
    List<Map<String,Object>> getReportCount4Origin(@Param("reportDefinedId") String reportDefinedId, @Param("sendDate") String sendDate);


    @Select("select " +
            "rsc.id," +
            "rsc.config_name," +
            "rsc.report_defined_id," +
            "rsc.sms_template_id," +
            "rst.template_name as sms_template_name," +
            "rsc.send_date," +
            "rsc.send_time," +
            "rsc.distance_type," +
            "rsc.distance_days," +
            "rsc.cross_holiday," +
            "rsc.send_status," +
            "rd.job_name report_defined_name" +
            " from report_sms_config rsc,rcd_job_config rd,rcd_sms_templates rst where rsc.report_defined_id = rd.job_id " +
            "and rsc.sms_template_id = rst.template_id")
    Page<ReportSmsConfig> pageSms(@Param("currPage") Integer currPage,@Param("pageSize") Integer pageSize);

    @Select("<script>" +
            "select * from rcd_sms_templates " +
            "<if test='smsTemplateId!=null'>" +
            " where template_id=#{smsTemplateId}" +
            "</if>" +
            "</script>")
    List<Map<String, Object>> getRcdSmsTemplates(@Param("smsTemplateId") String smsTemplateId);

    @Delete("delete from report_sms_config where id = #{smsId}")
    void deleteSmsConfig(@Param("smsId") String smsId);

    @Insert("insert into send_sms_record (sms_config_id,sms_config_name,phone_number,cust_name,send_result,faild_reason,send_context) " +
            "values (#{sms_config_id},#{sms_config_name},#{phone_number},#{cust_name},#{send_result},#{faild_reason},#{send_context})")
    void recordSmsSend(Map<String, String> sendParams);

    @Select("select " +
            "rsc.id," +
            "rsc.config_name," +
            "rsc.report_defined_id," +
            "rsc.sms_template_id," +
            "rsc.send_date," +
            "rsc.send_time," +
            "rsc.distance_type," +
            "rsc.distance_days," +
            "rsc.cross_holiday," +
            "rsc.send_status" +
            " from report_sms_config rsc where rsc.id=#{id} ")
    ReportSmsConfig getSmsJob(@Param("id") String id);
}
