package com.datarecord.webapp.notice.service.imp;

import com.datarecord.webapp.notice.dao.IReportSmsDao;
import com.datarecord.webapp.notice.entity.Holiday;
import com.datarecord.webapp.notice.entity.ReportSmsConfig;
import com.datarecord.webapp.notice.service.ReportSmsService;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.ReportJobInfo;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("reportSmsService")
public class ReportSmsServiceImp implements ReportSmsService {

    private final static String SEND_SUCCESS = "OK";

    private Logger logger = LoggerFactory.getLogger(ReportSmsServiceImp.class);

    @Autowired
    private IReportSmsDao reportSmsDao;

    @Autowired
    private RecordProcessService recordProcessService;

    private Map<String, ReportSmsConfig> reportSmsJobs = new HashMap<>();

    @PostConstruct
    public void initialize(){
        //获取当天所有的待执行任务
        this.listenSendJobs();
    }

    private void listenSendJobs(){

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<ReportSmsConfig> sendJobs = checkJobsToday();

                logger.info("开始执行发送短信提醒任务,待发送任务数量{}",sendJobs!=null?sendJobs.size():0);

                if(sendJobs!=null){
                    doSend(sendJobs);
                }
            }
        };

//        ScheduledExecutorService exceutor = Executors.newSingleThreadScheduledExecutor();
//        exceutor.scheduleAtFixedRate(runnable,40,180,TimeUnit.SECONDS);
    }

    @Transactional(rollbackFor = Exception.class)
    public void doSend(List<ReportSmsConfig> sendJobs){
        for (ReportSmsConfig sendJob : sendJobs) {
            Date sendTime = sendJob.getSend_time();

            logger.info("发送.....{}",sendJob.getConfig_name());
            logger.info("开始修改发送状态{}",sendJob.getConfig_name());
            changeJobStatus(sendJob.getId(),"1");//修改发送状态为发送中
            logger.debug("修改发送状态完成{}",sendJob.getConfig_name());
            sendSmsForCustomer(sendJob,sendTime);
            logger.info("开始修改发送状态为完成..{}",sendJob.getConfig_name());
            changeJobStatus(sendJob.getId(),"2");//修改发送状态为发送完毕
            logger.debug("修改发送状态完成{}",sendJob.getConfig_name());
        }
        logger.info("短信提醒发送完毕");
    }

    private List<ReportSmsConfig> checkJobsToday(){
        List<ReportSmsConfig> allSmsConfigs = reportSmsDao.getTodayReportSmsConfigs(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        return allSmsConfigs;
    }

    @Override
    public PageResult pagerSms(Integer currPage, Integer pageSize) {
        Page<ReportSmsConfig> reportSmsConfigPage = reportSmsDao.pageSms(currPage,pageSize);
        PageResult pageResult = PageResult.pageHelperList2PageResult(reportSmsConfigPage);
        return pageResult;
    }

    @Override
    public String createSmsJob(ReportSmsConfig reportSmsConfig) {
        String checkResult = this.saveSmsJobCheck(reportSmsConfig);
        if(!Strings.isNullOrEmpty(checkResult)){
            return checkResult;
        }

        try {
            String sendTimeStr = reportSmsConfig.getSend_time_str();
            if(!Strings.isNullOrEmpty(sendTimeStr)){
                reportSmsConfig.setSend_time(new SimpleDateFormat("HH:mm").parse(sendTimeStr));
            }
        } catch ( ParseException e) {
            e.printStackTrace();
        }
        reportSmsDao.saveSmsJob(reportSmsConfig);

        return null;
    }

    @Override
    public String updateSmsJob(ReportSmsConfig reportSmsConfig){
        String checkResult = this.saveSmsJobCheck(reportSmsConfig);
        if(!Strings.isNullOrEmpty(checkResult)){
            return checkResult;
        }

        try {
            String sendTimeStr = reportSmsConfig.getSend_time_str();
            if(!Strings.isNullOrEmpty(sendTimeStr)){
                reportSmsConfig.setSend_time(new SimpleDateFormat("HH:mm").parse(sendTimeStr));
            }
        } catch ( ParseException e) {
            e.printStackTrace();
        }
        reportSmsDao.updateSmsJob(reportSmsConfig);

        return null;
    }

    private String saveSmsJobCheck(ReportSmsConfig reportSmsConfig){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        JobConfig jobConfig = recordProcessService.getJobConfigByJobId(reportSmsConfig.getReport_defined_id());
        if(calendar.after(jobConfig.getJob_end_dt())){
            return "所有报表都已过期";
        }

        List<ReportJobInfo> reports = recordProcessService.getReportJobInfosByJobId(reportSmsConfig.getReport_defined_id());

        if(reports!=null&&reports.size()>0){
        }
        else{
            return "当前报表定义下无待填报报表";
        }
        return null;
    }

    private void checkSendDate(ReportSmsConfig reportSmsConfig,Date reportDataEnd){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String preDays = reportSmsConfig.getDistance_days();
        Integer preDaysInt = new Integer(preDays);
        String crossHoliday = reportSmsConfig.getCross_holiday();
        String reportDefinedID = reportSmsConfig.getReport_defined_id();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reportDataEnd);
        if(!Strings.isNullOrEmpty(crossHoliday)&&"N".equals(crossHoliday)){
            calendar.add(Calendar.HOUR,preDaysInt*(-24));
            Date senddate = calendar.getTime();
            String sendDateStr = format.format(senddate);
            logger.debug("send date is {}",sendDateStr);
            reportSmsConfig.setSend_date(senddate);
        }else{
            List<Holiday> allHolidays = reportSmsDao.getHolidays();
            List<String> holidayStrList = new ArrayList<>();

            for (Holiday holidayObj : allHolidays) {
                holidayObj.getHoliday_date();
                holidayStrList.add(format.format(holidayObj.getHoliday_date()));
            }

            for(int index=0;index<preDaysInt;index++){
                calendar.add(Calendar.HOUR,-24);
                if(holidayStrList.contains(format.format(calendar.getTime()))){
                    index--;
                    logger.debug("跳过日期{}，当前为假期",format.format(calendar.getTime()));
                }else{
                    if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ||
                            calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
                        index--;
                        logger.debug("跳过日期{}，当前为周末",format.format(calendar.getTime()));
                    }
                }
            }
            Date senddate = calendar.getTime();
            reportSmsConfig.setSend_date(senddate);

        }

    }

    @Override
    public String queryTemplateContext(String templateId) {

        String responseData = this.doSmsProcess("QuerySmsTemplate",templateId, new HashMap(),null);

        return responseData;
    }

    @Override
    public String sendSmsForCustomer(ReportSmsConfig sendJob,Date sendTime) {
        List<User> userList = reportSmsDao.getReportUsers(sendJob.getReport_defined_id());
        List<Map<String, Object>> reportCountData = reportSmsDao.getReportCount4Origin(sendJob.getReport_defined_id(),
                new SimpleDateFormat("yyyy-MM-dd").format(sendTime));

        Map<Integer,Long> originCountTMp = new HashMap<>();
        for (Map<String, Object> reportCountDatum : reportCountData) {
            Integer reportOriginId = (Integer) reportCountDatum.get("report_origin");
            Long reportCount = (Long) reportCountDatum.get("report_count");
            originCountTMp.put(reportOriginId,reportCount);
        }

        for (User user : userList) {
            BigInteger userOriginId = user.getOrigin_id();
            String phoneNo = user.getMobile_phone();
            Map<String,Object> sendParams = new HashMap<>();
            sendParams.put("count",originCountTMp.get(userOriginId));
            sendParams.put("dueDate",new SimpleDateFormat("yyyy-MM-dd").format(sendTime));

            Map<String,String> sendRecordMap = new HashMap<>();
            sendRecordMap.put("sms_config_id",String.valueOf(sendJob.getId()));
            sendRecordMap.put("sms_config_name",sendJob.getConfig_name());
            sendRecordMap.put("phone_number",phoneNo);
            sendRecordMap.put("cust_name",user.getUser_name_cn());
            if(!Strings.isNullOrEmpty(phoneNo)){
                String response = this.doSmsProcess("SendSms",sendJob.getSms_template_id(), sendParams,phoneNo);
                if(!Strings.isNullOrEmpty(response)){
                    HashMap responseMap = JsonSupport.jsonToMap(response);
                    if(responseMap.containsKey("Code")){
                        String aliRespnseCode = (String) responseMap.get("Code");

                        String sendContext = JsonSupport.objectToJson(sendParams);
                        sendRecordMap.put("send_context",sendContext);

                        if(!"OK".equals(aliRespnseCode)){
                            sendRecordMap.put("send_result","FAILED");
//                        JsonSupport.objectToJson(response)
                            sendRecordMap.put("faild_reason",response);
                        }else{
                            sendRecordMap.put("send_result","SUCCESS");
                        }

                    }
                }
                logger.debug("发送信息结果:{}",response);
            }else{
                String sendContext = JsonSupport.objectToJson(sendParams);
                sendRecordMap.put("send_context",sendContext);
                sendRecordMap.put("send_result","FAILED");
                sendRecordMap.put("faild_reason","手机号为空");
            }

            reportSmsDao.recordSmsSend(sendRecordMap);
//            logger.debug("向电话号{},发送短信{}",phoneNo,sendParams.toString());
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> getAliSmsTemplates() {
        List<Map<String, Object>> tempaltes = reportSmsDao.getAliSmsTemplates();
        return tempaltes;
    }

    @Override
    public void deleteSmsJob(String smsId) {
        reportSmsDao.deleteSmsConfig(smsId);
    }


    @Override
    public String doSmsProcess(String processName,String smsTemplatId,Map<String,Object> sendParams,String phoneNum){

        return null;
    }

    @Override
    public ReportSmsConfig getSmsJob(String id) {
        ReportSmsConfig reportSmsConfig = reportSmsDao.getSmsJob(id);
        return reportSmsConfig;
    }


    public void changeJobStatus(Integer configId,String status) {
        reportSmsDao.changeJobStatus(configId,status);
    }
}
