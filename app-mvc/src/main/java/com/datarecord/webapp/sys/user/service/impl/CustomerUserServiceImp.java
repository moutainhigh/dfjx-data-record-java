package com.datarecord.webapp.sys.user.service.impl;

import com.datarecord.webapp.sys.origin.entity.ChinaAreaCode;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.entity.OriginNature;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.sys.user.dao.ICustomerUserDao;
import com.datarecord.webapp.sys.user.entity.CustomerUser;
import com.datarecord.webapp.sys.user.entity.UserForgetPwdRecord;
import com.datarecord.webapp.sys.user.service.CustomerUserService;
import com.github.pagehelper.Page;
import com.google.common.base.Strings;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import com.workbench.auth.user.entity.UserStatus;
import com.workbench.auth.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("cqnyUserService")
public class CustomerUserServiceImp implements CustomerUserService {

    @Autowired
    private ICustomerUserDao cqnyUserDao;

    @Autowired
    private OriginService originService;

    @Autowired
    private UserService userService;


    private static Logger logger = LoggerFactory.getLogger(CustomerUserServiceImp.class);

    @Override
    public PageResult pageCqnyUser(Integer currPage, Integer pageSize, String user_name, String user_type, List<Integer> originList) {
        Page<CustomerUser> userPage = cqnyUserDao.pageCqnyUser(currPage, pageSize, Strings.emptyToNull(user_name), user_type, originList);

        PageResult pageResult = PageResult.pageHelperList2PageResult(userPage);
        
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectOriginType(String userId, String originType) {
        //获取到该用户对应的机构
        Origin originByUser = originService.getOriginByUser(new Integer(userId));
        //获取到机构下的所有用户
        List<User> allUsers = originService.getUsersByOrigin(originByUser.getOrigin_id());
        //修改该机构下的所有用户状态为 征程
        if(allUsers!=null){
            for (User userObj : allUsers) {
                userObj.setUser_status(String.valueOf(UserStatus.NORMAL.getStatus()));
                userService.updateUser(userObj);
            }
        }
        //修改机构的类型
        originByUser.setOrigin_type(originType);
        originService.updateOrigin(originByUser);

        //检查该机构下的报表，如果报表类型与修改后的机构类型不符。将报表置为失效
//        List<Integer> origins = new ArrayList<>();
//        origins.add(originByUser.getOrigin_id());
//        List<ReportCustomer> reportCustomers = reportCustomerService.allReportForOrigin(originByUser.getOrigin_id().toString());
//        if(reportCustomers!=null){
//            for (ReportCustomer reportCustomer : reportCustomers) {
//                String oldStatus = reportCustomer.getReport_status();
//
//                String reportType = reportCustomer.getReport_type();
//                if(reportType.equals("0")){//所有企业都要填的报表
//                    if(ReportStatus.REMOVE.compareTo(oldStatus)){
//                        reportCustomerService.updateReportCustomerStatus(
//                                reportCustomer.getReport_id().toString(), ReportStatus.NORMAL);
//                    }
//                }else{
//                    if(!reportType.equals(originType)){
//                        reportCustomerService.updateReportCustomerStatus(
//                                reportCustomer.getReport_id().toString(), ReportStatus.REMOVE);
//                    }else{
//                        if(ReportStatus.REMOVE.compareTo(oldStatus)){
//                            reportCustomerService.updateReportCustomerStatus(
//                                    reportCustomer.getReport_id().toString(), ReportStatus.NORMAL);
//                        }
//                    }
//                }
//            }
//        }

    }


    @Override
    public Map<String, String> getSmsValidateCode(Integer userId, String phone_num) {
//
//        String validateCode = this.getValidateCode(6);
//
//        logger.debug("发送验证码：{}",validateCode);
//
//        Map codeMap = new HashMap();
//        codeMap.put("code",validateCode);
//        String response = reportSmsService.doSmsProcess("SendSms",aliSmsConfig.getForgetPwdTemplage(),codeMap,phone_num);
//
//        Map<String,String> checkResult = new HashMap<>();
//
//        if(!Strings.isNullOrEmpty(response)){
//            HashMap responseMap = JsonSupport.jsonToMap(response);
//            if(responseMap.containsKey("Code")){
//                String aliRespnseCode = (String) responseMap.get("Code");
//
//                if("OK".equals(aliRespnseCode)){
//                    checkResult.put("result", JsonResult.RESULT.SUCCESS.toString());
//                    checkResult.put("message", validateCode);
//                    cqnyUserDao.updateSmsValidateCode(userId,validateCode,new Date());
//
//                }else{
//                    checkResult.put("result", JsonResult.RESULT.FAILD.toString());
//                    checkResult.put("message", String.valueOf(responseMap.get("Message")));
//                }
//
//            }
//        }
//        logger.debug("发送信息结果:{}",response);
//
//        return checkResult;
        return null;
    }

    @Override
    public UserForgetPwdRecord getUserForgetPwdRecord(Integer userId){
        UserForgetPwdRecord userForgetPwdRecord = cqnyUserDao.getUserForgetPwdRecord(userId);
        return userForgetPwdRecord;
    }

    @Override
    public String updateValidateCode(Integer user_id) {
        String valiteCode = this.getValidateCode(4);
        cqnyUserDao.updateValidateCode(user_id,valiteCode,new Date());
        return valiteCode;
    }

    @Override
    public String newValidateCode(Integer user_id) {
        String valiteCode = this.getValidateCode(4);
        cqnyUserDao.newValidateCode(user_id,valiteCode,new Date());
        return valiteCode;    }

    @Override
    public List<ChinaAreaCode> getAreaData(String parentId) {
        List<ChinaAreaCode> resultLIst = cqnyUserDao.getAreaCodeList(parentId);
        return resultLIst;
    }

    @Override
    public List<OriginNature> getOriginNature() {
        List<OriginNature> resultLIst = cqnyUserDao.getOriginNature();
        return resultLIst;
    }

    private String getValidateCode(int codeLength){
        StringBuilder sb = new StringBuilder();
        Random random = new Random();

        for(int i=0;i<codeLength;i++){
            int randomInt = random.nextInt(10);
            sb.append(randomInt);
        }

        return sb.toString();
    }
}
