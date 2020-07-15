package com.datarecord.webapp.notice.service;

import com.datarecord.webapp.notice.entity.ReportSmsConfig;
import com.webapp.support.page.PageResult;
import org.apache.axis2.AxisFault;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ReportSmsService {

    PageResult pagerSms(Integer currPage, Integer pageSize);

    String createSmsJob(ReportSmsConfig reportSmsConfig);

    String updateSmsJob(ReportSmsConfig reportSmsConfig);

    String queryTemplateContext(String templateId) throws AxisFault;

    String sendSmsForCustomer(ReportSmsConfig sendJob);

    List<Map<String, Object>> getAliSmsTemplates();

    void deleteSmsJob(String smsId);

    String doSmsProcess(String processName,Map<String,Object> smsTemplat,Map<String,Object> sendParams,String phoneNum) throws AxisFault;

    ReportSmsConfig getSmsJob(String id);
}
