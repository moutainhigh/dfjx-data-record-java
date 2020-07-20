package com.datarecord.webapp.notice.service.imp;

import com.datarecord.webapp.fillinatask.bean.JobInteval;
import com.datarecord.webapp.fillinatask.service.JobConfigService;
import com.datarecord.webapp.notice.dao.IReportSmsDao;
import com.datarecord.webapp.notice.entity.ReportSmsConfig;
import com.datarecord.webapp.notice.entity.SmsMessageInfo;
import com.datarecord.webapp.notice.entity.SmsServiceConfig;
import com.datarecord.webapp.notice.service.ReportSmsService;
import com.datarecord.webapp.process.entity.JobConfig;
import com.datarecord.webapp.process.entity.ReportJobInfo;
import com.datarecord.webapp.process.service.RecordProcessService;
import com.datarecord.webapp.rcduser.service.RecordUserService;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.page.PageResult;
import com.webapp.support.utils.DateSupport;
import com.workbench.auth.user.entity.User;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.rpc.client.RPCServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service("reportSmsService")
public class ReportSmsServiceImp implements ReportSmsService {

    private final static String SEND_SUCCESS = "OK";

    private Logger logger = LoggerFactory.getLogger(ReportSmsServiceImp.class);

    @Autowired
    private IReportSmsDao reportSmsDao;

    @Autowired
    private RecordProcessService recordProcessService;

    @Autowired
    private JobConfigService jobConfigService;

    @Autowired
    private RecordUserService recordUserService;

    @Autowired
    private SmsServiceConfig smsServiceConfig;

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
                List<ReportSmsConfig> sendJobs = checkSendJobs();

                logger.info("开始执行发送短信提醒任务,待发送任务数量{}",sendJobs!=null?sendJobs.size():0);

                if(sendJobs!=null){
                    doSend(sendJobs);
                }
            }
        };

        Calendar calendar = DateSupport.getBeijingCalendar();
        int nowMiute = calendar.get(Calendar.MINUTE);
        int firstSendMiute = 0;
        if(nowMiute>=30){
            firstSendMiute = 60 - nowMiute;
        }else{
            firstSendMiute = 30 - nowMiute;
        }
        logger.info("首次发送短信将在:{}分钟后开始,之后每隔:{}分钟发送一次短信",firstSendMiute,30);

        ScheduledExecutorService exceutor = Executors.newSingleThreadScheduledExecutor();
        exceutor.scheduleAtFixedRate(runnable,firstSendMiute,30, TimeUnit.MINUTES);
    }

    @Transactional(rollbackFor = Exception.class)
    public void doSend(List<ReportSmsConfig> sendJobs){
        for (ReportSmsConfig sendJob : sendJobs) {
            Date sendTime = sendJob.getSend_time();

            logger.info("发送.....{}",sendJob.getConfig_name());
            logger.info("开始修改发送状态{}",sendJob.getConfig_name());
            changeJobStatus(sendJob.getId(),"1");//修改发送状态为发送中
            logger.debug("修改发送状态完成{}",sendJob.getConfig_name());
            logger.info("短信中心配置信息:{}",smsServiceConfig.toString());
            sendSmsForCustomer(sendJob);
            logger.info("开始修改发送状态为完成..{}",sendJob.getConfig_name());
            changeJobStatus(sendJob.getId(),"2");//修改发送状态为发送完毕
            logger.debug("修改发送状态完成{}",sendJob.getConfig_name());
        }
        logger.info("短信提醒发送完毕");
    }

    private List<ReportSmsConfig> checkSendJobs(){
        List<ReportSmsConfig> sendList= new ArrayList<>();
        Page<ReportSmsConfig> allSmsJob = reportSmsDao.pageSms(null,null,"0");
        for (ReportSmsConfig reportSmsConfig : allSmsJob) {
            if(this.needSendMsg(reportSmsConfig)){
//            if(true){
                sendList.add(reportSmsConfig);
            }
        }
        return sendList;
    }

    /**
     *  '1':'填报周期开始前',
     *  '2':'填报周期开始后',
     *  '3':'填报周期结束前',
     *  '4':'填报周期结束后'
     * @param reportSmsConfig
     * @return
     */
    private boolean needSendMsg(ReportSmsConfig reportSmsConfig){
        Calendar calenday = Calendar.getInstance();
        Integer today = calenday.get(Calendar.DAY_OF_MONTH);
        Integer HOUR = calenday.get(Calendar.HOUR_OF_DAY);
        Integer MINUTE = calenday.get(Calendar.MINUTE);
        String distanceType = reportSmsConfig.getDistance_type();
        String distanceDays = reportSmsConfig.getDistance_days();
        Date sendDate = reportSmsConfig.getSend_time();
        if(sendDate==null){
            logger.error("当前发送任务的发送时间没有设定，不予发送:{}",reportSmsConfig.getConfig_name());
            return false;
        }
        calenday.setTime(sendDate);
        if(Strings.isNullOrEmpty(distanceDays)){
            logger.error("当前发送任务的发送天数没有设定，不予发送:{}",reportSmsConfig.getConfig_name());
            return false;
        }
        String jobId = reportSmsConfig.getReport_defined_id();
        List<JobInteval> jobIntevals = jobConfigService.getJobIntevals(jobId);
        for (JobInteval jobInteval : jobIntevals) {
            String intervalStart = jobInteval.getJob_interval_start();
            String intervalEnd = jobInteval.getJob_interval_end();
            if(Strings.isNullOrEmpty(intervalStart)||Strings.isNullOrEmpty(intervalEnd))
                return false;
            if("1".equalsIgnoreCase(distanceType)){
                calenday.set(Calendar.DAY_OF_MONTH,new Integer(intervalStart));
                calenday.add(Calendar.DAY_OF_MONTH,0 - new Integer(distanceDays));
            }else if("2".equalsIgnoreCase(distanceType)){
                calenday.set(Calendar.DAY_OF_MONTH,new Integer(intervalStart));
                calenday.add(Calendar.DAY_OF_MONTH,new Integer(distanceDays));
            }else if("3".equalsIgnoreCase(distanceType)){
                calenday.set(Calendar.DAY_OF_MONTH,new Integer(intervalEnd));
                calenday.add(Calendar.DAY_OF_MONTH,0 - new Integer(distanceDays));
            }else if("4".equalsIgnoreCase(distanceType)){
                calenday.set(Calendar.DAY_OF_MONTH,new Integer(intervalEnd));
                calenday.add(Calendar.DAY_OF_MONTH,new Integer(distanceDays));
            }
            logger.info("当前时间:{}号,{}:{},该任务在:{}号{}:{}会被发送。",
                    today,HOUR,MINUTE,
                    calenday.get(Calendar.DAY_OF_MONTH),calenday.get(Calendar.HOUR_OF_DAY),calenday.get(Calendar.MINUTE));

            if(calenday.get(Calendar.DAY_OF_MONTH) == today){
                //小时是否对的上
                if(HOUR == calenday.get(Calendar.HOUR_OF_DAY)){
                    if(MINUTE<=30){
                        if(Calendar.MINUTE==0){
                            return true;
                        }
                    }else{
                        if(Calendar.MINUTE==60){
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    @Override
    public PageResult pagerSms(Integer currPage, Integer pageSize) {
        Page<ReportSmsConfig> reportSmsConfigPage = reportSmsDao.pageSms(currPage,pageSize,null);
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

    @Override
    public String queryTemplateContext(String templateId) throws AxisFault {

        String responseData = this.doSmsProcess("QuerySmsTemplate",null, new HashMap(),null);

        return responseData;
    }

    @Override
    public String sendSmsForCustomer(ReportSmsConfig sendJob) {
        PageResult pageData = recordUserService.checkedOriginUser(null, null, sendJob.getReport_defined_id(), null);
        List<User> userList = pageData.getDataList();
        String smsTemplateId = sendJob.getSms_template_id();
//        template_content
        List<Map<String, Object>> templateList = reportSmsDao.getRcdSmsTemplates(smsTemplateId);
        JobConfig jobConfig = jobConfigService.getJobConfig(sendJob.getReport_defined_id());
        List<JobInteval> jobIntevals = jobConfigService.getJobIntevals(sendJob.getReport_defined_id());
        StringBuilder sb = new StringBuilder();
        sb.append("填报日期:");
        for (JobInteval jobInteval : jobIntevals) {
            sb.append(" ").append(jobInteval.getJob_interval_start()).append("号-").append(jobInteval.getJob_interval_end()).append("号");
        }

        for (User user : userList) {
            BigInteger userOriginId = user.getOrigin_id();
            String phoneNo = user.getMobile_phone();
            Map<String,Object> sendParams = new HashMap<>();
            sendParams.put("jobName",jobConfig.getJob_name());
            sendParams.put("reportDate",sb.toString());

            Map<String,String> sendRecordMap = new HashMap<>();
            sendRecordMap.put("sms_config_id",String.valueOf(sendJob.getId()));
            sendRecordMap.put("sms_config_name",sendJob.getConfig_name());
            sendRecordMap.put("phone_number",phoneNo);
            sendRecordMap.put("cust_name",user.getUser_name_cn());
            try{
                if(!Strings.isNullOrEmpty(phoneNo)){
                    String response = this.doSmsProcess("SendSms",templateList.get(0), sendParams,phoneNo);
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
            }catch (RuntimeException | AxisFault e){
                e.printStackTrace();
                sendRecordMap.put("send_result","FAILED");
                sendRecordMap.put("faild_reason",e.getMessage());
            }
            reportSmsDao.recordSmsSend(sendRecordMap);
//            logger.debug("向电话号{},发送短信{}",phoneNo,sendParams.toString());
        }

        return null;
    }

    @Override
    public List<Map<String, Object>> getAliSmsTemplates() {
        List<Map<String, Object>> tempaltes = reportSmsDao.getRcdSmsTemplates(null);
        return tempaltes;
    }

    @Override
    public void deleteSmsJob(String smsId) {
        reportSmsDao.deleteSmsConfig(smsId);
    }


    @Override
    public String doSmsProcess(String processName,Map<String, Object> smsTemplate,Map<String,Object> sendParams,String phoneNum) throws AxisFault {
        String sendMsg = (String) smsTemplate.get("template_content");
        if(sendParams!=null&&sendParams.size()>0){
            for (String paramKey : sendParams.keySet()) {
                Object paramValue = sendParams.get(paramKey);
                sendMsg = sendMsg.replace("${"+paramKey+"}",String.valueOf(paramValue));
            }
        }else{
        }

        StringBuilder sendUrlSb = new StringBuilder().append(smsServiceConfig.getHost()).append(":").append(smsServiceConfig.getPort()).append("/")
                .append(smsServiceConfig.getAppname()).append("/").append(smsServiceConfig.getWsdlUrl());


        String sq = this.randomCode(3);  //必须三位随机数
        String smsid = this.randomCode(36);	//必须36位随机字符串 短信回复里面会用到
        String[] phones = {phoneNum};
        //发送短信
        logger.debug("发送短信:{}******{}******{}******{}*****{}",sq,smsid,phones,sendMsg,sendUrlSb.toString());
        String sendXml = this.getXmlByAppMessage(smsid,sq,phones,sendMsg);

        Object[] resultArray = this.sendMessageByTaiji(smsServiceConfig.getUser(), sendXml,new Integer(smsServiceConfig.getClientcode()),sendUrlSb.toString());
        String responseResult = null;
        if(resultArray!=null&&resultArray.length>0){
            for (Object result : resultArray) {
                Map messageMap = this.getAppMessageXmlOut(String.valueOf(result));
                List<SmsMessageInfo> smsMessageInfos = (List) messageMap.get("SmsMessageInfos");
                for (SmsMessageInfo smsMessageInfo : smsMessageInfos) {
                    if(smsMessageInfo!=null){
                        logger.info("短信网关返回发送结果信息:{}",smsMessageInfo);
                        responseResult = JsonSupport.objectToJson(smsMessageInfo);
                    }
                }
            }
        }
        return responseResult;
        //获取短信回复
		/*Object[] result2 = MessageUtil.getMessageByTaiji(usercode,appId,wsdl);
		String xml2 = (String) result2[0];
		Map messageMap = MessageUtil.getAppMessageXmlOut(xml2);
		List<SmsMessageInfo> SmsMessageInfos = (List) messageMap.get("SmsMessageInfos");
		for (int i = 0; i < SmsMessageInfos.size(); i++) {
			SmsMessageInfo SmsMessageInfo = distance_dayss.get(i);
		}*/
    }

    @Override
    public ReportSmsConfig getSmsJob(String id) {
        ReportSmsConfig reportSmsConfig = reportSmsDao.getSmsJob(id);
        return reportSmsConfig;
    }

    @Override
    public void updateConfigStatus(String smsId, String config_status) {
        reportSmsDao.updateConfigStatus(smsId,config_status);
    }


    public void changeJobStatus(Integer configId,String status) {
        reportSmsDao.changeJobStatus(configId,status);
    }

    //生成调用短信的xml
    public String getXmlByAppMessage(String smsId, String sendNum,
                                            String[] phones, String mContent) {
        StringBuffer sb = new StringBuffer(
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        sb.append("<SMS type=\"sendSms\">\r\n");
        for (String string : phones) {
            sb.append("<Message SmsID=\"" + smsId + "\" SendNum=\"" + sendNum
                    + "\" RecvNum=\"" + string + "\" ");
            sb.append("Content=\"" + mContent + "\"/>\r\n");
        }
        sb.append("</SMS>");
        return sb.toString();
    }
    //调用短信接口
    public Object[] sendMessageByTaiji(String userCode, String xml,int appId,String wsdl) throws AxisFault {
        Object[] result = null;
        logger.info("发送信息，信息内容:{}",xml);
        logger.info("发送信息，短信服务地址:{}",wsdl);
        logger.info("发送信息，appId:{}",appId);
        logger.info("发送信息，userCode:{}",userCode);

        RPCServiceClient client;
        client = new RPCServiceClient();
        Options options = client.getOptions();
        options.setProperty(org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT,new Integer(smsServiceConfig.getTimeout()));
        EndpointReference epr = new EndpointReference(wsdl);
        options.setTo(epr);
//        QName qname = new QName("http://172.25.13.128:8080/webIscp/services/messageInfoService?wsdl","sendMessageInfo");
        QName qname = new QName(smsServiceConfig.getSmsServiceHost(),smsServiceConfig.getSmsServicSendurl());

        logger.info("发送短信请求,{},{}",smsServiceConfig.getSmsServiceHost(),smsServiceConfig.getSmsServicSendurl());

        Object[] objEntryArgs = new Object[] { appId, userCode, xml };
        Class[] returnTypes = new Class[] { String.class };
        result = client.invokeBlocking(qname, objEntryArgs, returnTypes);

        logger.info("发送短信返回数据:{}",result);

//        RPCServiceClient client;
//        client = new RPCServiceClient();
////        Options options = client.getOptions();
////        options.setProperty(HTTPConstants.CONNECTION_TIMEOUT, new Integer(smsServiceConfig.getTimeout())*1000);
////        EndpointReference epr = new EndpointReference(wsdl);
////        options.setTo(epr);
//        QName qname = new QName(smsServiceConfig.getSmsServiceHost(),smsServiceConfig.getSmsServicSendurl());
//
//        Object[] objEntryArgs = new Object[] { appId, userCode, xml };
//        Class[] returnTypes = new Class[] { String.class };
//        result = client.invokeBlocking(qname, objEntryArgs, returnTypes);

        return result;
    }
//    //获取短信回复
//    public static Object[] getMessageByTaiji(String userCode,int appId,String wsdl) {
//        RPCServiceClient client;
//        Object[] result = null;
//        try {
//            client = new RPCServiceClient();
//            EndpointReference epr = new EndpointReference(wsdl);
//            Options options = client.getOptions();
//            options.setProperty(
//                    org.apache.axis2.transport.http.HTTPConstants.CONNECTION_TIMEOUT,
//                    new Integer(48000000));
//            options.setTo(epr);
//            Object[] objEntryArgs = new Object[] { appId, userCode }; // 发送端
//            QName qname = new QName("http://service.auth.taiji.com",
//                    "getdistance_days");
//            Class[] classes = new Class[] { String.class };
//            result = client.invokeBlocking(qname, objEntryArgs, classes);
//        } catch (AxisFault e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }
    public Map getAppMessageXmlOut(String xml) {
        Map map = new HashMap();
        Document doc = null;
        try {
            // 将字符串转为XML
            doc = DocumentHelper.parseText(xml);
            // 获取根节点
            Element rootElt = doc.getRootElement();
            // 拿到根节点的名称
            // 获取根节点下的子节点xml
            String type=rootElt.attributeValue("type");
            map.put("type", type);
            Iterator iter = rootElt.elementIterator("Message");
            List<SmsMessageInfo> SmsMessageInfos=new ArrayList<SmsMessageInfo>();
            // 遍历xml节点
            while (iter.hasNext()) {
                Element recordEle = (Element) iter.next();
                // 拿到xml节点下的子节点userCode值
//                Element message = recordEle.element("Message");
                SmsMessageInfo SmsMessageInfo= new SmsMessageInfo();
                String SmsId=recordEle.attributeValue("SmsID");
                SmsMessageInfo.setSmsid(SmsId);
                String SendNum=recordEle.attributeValue("SendNum");
                SmsMessageInfo.setSendnum(SendNum);
                String RecvNum=recordEle.attributeValue("RecvNum");
                SmsMessageInfo.setRecvnum(RecvNum);
                String Content=recordEle.attributeValue("Content");
                SmsMessageInfo.setContent(Content);
                String recvTime=recordEle.attributeValue("RecvTime");
                SmsMessageInfo.setRecvtime(recvTime);
                SmsMessageInfos.add(SmsMessageInfo);
            }
            map.put("SmsMessageInfos", SmsMessageInfos);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    private String randomCode(Integer codeLength){
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        if(codeLength!=null&&codeLength>0){
            for (Integer index=0 ; index<codeLength;index++){
                sb.append(random.nextInt(9));
            }
        }
        return sb.toString();
    }
}
