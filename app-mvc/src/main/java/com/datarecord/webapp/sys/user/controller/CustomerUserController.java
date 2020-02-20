package com.datarecord.webapp.sys.user.controller;

import com.datarecord.webapp.sys.origin.entity.ChinaAreaCode;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.entity.OriginNature;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.datarecord.webapp.sys.user.entity.CustomerUser;
import com.datarecord.webapp.sys.user.entity.UserForgetPwdRecord;
import com.datarecord.webapp.sys.user.service.CustomerUserService;
import com.google.common.base.Strings;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.webapp.support.page.PageResult;
import com.webapp.support.session.SessionSupport;
import com.workbench.auth.user.entity.User;
import com.workbench.auth.user.entity.UserStatus;
import com.workbench.auth.user.service.UserService;
import com.workbench.shiro.WorkbenchShiroUtils;
import com.workbench.spring.aop.annotation.JsonpCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("sys/custUser")
public class CustomerUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CustomerUserService customerUserService;

    @Autowired
    private OriginService originService;

    @RequestMapping("listUserPage")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getUserByPage(int currPage, int pageSize, User user, String searchOriginId, String searchOriginName) {
        List<Origin> allReportOrigins = originService.listAllOrigin();

        List<Integer> originSqlParams = new ArrayList<>();

//        List<Integer> originParams = new ArrayList<>();
        if (Strings.isNullOrEmpty(searchOriginId) && Strings.isNullOrEmpty(searchOriginName)) {//全量

        } else if (!Strings.isNullOrEmpty(searchOriginId)) {//有机构查询条件
            originSqlParams.add(new Integer(searchOriginId));
            List<Origin> allChildrenOrigins = originService.checkoutSons(new Integer(searchOriginId), allReportOrigins);
            for (Origin originChild : allChildrenOrigins) {
                originSqlParams.add(originChild.getOrigin_id());
            }
        }

        if (!Strings.isNullOrEmpty(searchOriginName)) {
            List<Origin> origins = originService.getOriginByName(searchOriginName);
            List<Integer> originParamsTmp = new ArrayList<>();

            for (Origin origin : origins) {
                Integer originObj = origin.getOrigin_id();
                if (originSqlParams != null && originSqlParams.size() > 0) {
                    if (originSqlParams.contains(originObj)) {
                        originParamsTmp.add(originObj);
                    }
                } else {
                    originParamsTmp.add(originObj);
                }
            }
            originSqlParams = originParamsTmp;

        }

        PageResult pageResult = customerUserService.pageCqnyUser(currPage,
                pageSize,
                user.getUser_name_cn(),
                user.getUser_type(),
                originSqlParams != null && originSqlParams.size() > 0 ? originSqlParams : null);

        List<CustomerUser> cqnyResultData = pageResult.getDataList();
        for (CustomerUser userTmp : cqnyResultData) {
            Integer reportOriginId = userTmp.getOrigin_id();

            Map<String, Origin> result = originService.getFist2Origin(reportOriginId, allReportOrigins);
            if (result.get("cityOrigin") != null)
                userTmp.setOrigin_city(result.get("cityOrigin").getOrigin_name());
            if (result.get("provinceOrigin") != null)
                userTmp.setOrigin_province(result.get("provinceOrigin").getOrigin_name());
        }

        Collection<Map<String, Object>> first2Origin = originService.checkProvAndCity(allReportOrigins);


        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("currPage", pageResult.getCurrPage());
//        responseMap.put("dataList",resultData);
        responseMap.put("dataList", cqnyResultData);
        responseMap.put("pageSize", pageResult.getPageSize());
        responseMap.put("totalNum", pageResult.getTotalNum());
        responseMap.put("totalPage", pageResult.getTotalPage());
        responseMap.put("first2Origin", first2Origin);

        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null, responseMap);
        return response;
//
//
//        String jsonResult = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取成功", null, pageResult);
//        return jsonResult;
    }

    @RequestMapping("resetPwd")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult resetPwd(String userId) {
        if(!Strings.isNullOrEmpty(userId)){
            userService.resetPwd(new Integer(userId));
        }

        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功", null, null);
        return response;
    }

    @RequestMapping("selectOriginType")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult selectOriginType(@RequestBody Map<String,Object> selectOriginMap){
//        String userId, String user_name, String origin_type
        String userId = selectOriginMap.containsKey("userId")?(String)selectOriginMap.get("userId"):null;
        String user_name = selectOriginMap.containsKey("user_name")?(String)selectOriginMap.get("user_name"):null;
        String origin_type = selectOriginMap.containsKey("origin_type")?(String)selectOriginMap.get("origin_type"):null;


        if(Strings.isNullOrEmpty(userId)){
            if(!Strings.isNullOrEmpty(user_name)){
                User user = WorkbenchShiroUtils.checkUserFromShiroContext();
                if(user.getUser_name().equals(user_name)){
                    userId = String.valueOf(user.getUser_id());
                }else{
                    user = userService.getUserByUserNm(user_name);
                    if(user!=null){
                        userId = String.valueOf(user.getUser_id());
                    }else{
                        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "用户未找到", "用户未找到",
                                "USER_NOT_NULL");
                        return response;
                    }
                }
            }else{
                JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "用户为空", "用户为空",
                        "USER_NULL");
                return response;
            }
        }

        if(Strings.isNullOrEmpty(origin_type)){
            JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "请选择企业类型", "请选择企业类型",
                    "ORIGIN_TYPE_NULL");
            return response;
        }

        customerUserService.selectOriginType(userId,origin_type);

        changeUserInfo(selectOriginMap,userId);

        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "修改成功",
                null, JsonResult.RESULT.SUCCESS);
        return response;
    }

    @RequestMapping("changeSelfOriginType")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult changeSelfOriginType(@RequestBody Map<String,Object> requestMap){
        String origin_type = (String) requestMap.get("origin_type");
        User currUser = WorkbenchShiroUtils.checkUserFromShiroContext();
//        Map<String,Object> selectOriginMap = new HashMap<>();
        requestMap.put("userId",String.valueOf(currUser.getUser_id()));
        requestMap.put("origin_type",origin_type);

        Origin originObj = originService.getOriginByUser(currUser.getUser_id());
        if(requestMap.get("origin_address_province")!=null){
            originObj.setOrigin_address_province(String.valueOf(requestMap.get("origin_address_province")));
        }
        if(requestMap.get("origin_address_city")!=null){
            originObj.setOrigin_address_city(String.valueOf(requestMap.get("origin_address_city")));
        }
        if(requestMap.get("origin_address_area")!=null){
            originObj.setOrigin_address_area(String.valueOf(requestMap.get("origin_address_area")));
        }
        if(requestMap.get("origin_address_street")!=null){
            originObj.setOrigin_address_street(String.valueOf(requestMap.get("origin_address_street")));
        }
        if(requestMap.get("origin_address_detail")!=null){
            originObj.setOrigin_address_detail(String.valueOf(requestMap.get("origin_address_detail")));
        }
        if(requestMap.get("origin_address")!=null){
            originObj.setOrigin_address(String.valueOf(requestMap.get("origin_address")));
        }
        if(requestMap.get("origin_nature")!=null){
            originObj.setOrigin_nature(String.valueOf(requestMap.get("origin_nature")));
        }

        originService.updateOrigin(originObj);

        return this.selectOriginType(requestMap);
    }

    @RequestMapping("getCurrUserOrigin")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getCurrUserOrigin(){
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        user = userService.getUserByUserId(user.getUser_id());
        Origin origin = originService.getOriginByUser(user.getUser_id());
        Map<String,Object> responeMap = new HashMap<>();
        responeMap.put("origin",origin);
        responeMap.put("user",user);
        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功",
                null, responeMap);
        return response;
    }

    @RequestMapping("getUserInfo")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getUserInfo(String user_name){
        User userInfo = userService.getUserByUserNm(user_name);
        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功",
                null, userInfo);
        return response;
    }

    @RequestMapping("getValidateCode")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getValidateCode(String user_name){
        User userInfo = userService.getUserByUserNm(user_name);
        if(userInfo!=null){
            UserForgetPwdRecord userForgetpwdRecord = customerUserService.getUserForgetPwdRecord(userInfo.getUser_id());
            String valiteCode = null;
            if(userForgetpwdRecord!=null){
                valiteCode = customerUserService.updateValidateCode(userInfo.getUser_id());
            }else{
                valiteCode = customerUserService.newValidateCode(userInfo.getUser_id());
            }

            JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, valiteCode,
                    null, valiteCode);
            return response;

        }else{
            JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "未找到该用户",
                    null, "未找到该用户");
            return response;
        }
    }


    @RequestMapping("getSmsValidateCode")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getSmsValidateCode(String phone_num, String user_name, String validate_code){

        User userInfo = userService.getUserByUserNm(user_name);
        if(userInfo!=null){
            String mobilePhone = userInfo.getMobile_phone();
            if(!Strings.isNullOrEmpty(mobilePhone)&&mobilePhone.equals(phone_num)){
                //检查用户输入验证码是否正确

                if(Strings.isNullOrEmpty(validate_code)){
                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "请输入图形验证码",
                            null, "请输入图形验证码");
                    return response;
                }

                UserForgetPwdRecord userForgetpwdRecord = customerUserService.getUserForgetPwdRecord(userInfo.getUser_id());
                if(userForgetpwdRecord==null){
                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "请输入图形验证码",
                            null, "请输入图形验证码");
                    return response;
                }else{
                    if(!userForgetpwdRecord.getValidate_code().equals(validate_code)){
                        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "图形验证码输入错误",
                                null, "图形验证码输入错误");
                        return response;
                    }else{
                        Date validateCodeTime = userForgetpwdRecord.getValidate_code_time();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(validateCodeTime);
                        calendar.add(Calendar.SECOND,60*5);
                        Calendar currCalendar = Calendar.getInstance();
                        currCalendar.setTime(new Date());
                        if(calendar.before(currCalendar)){
                            JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "图形验证码已过期，请重新输入",
                                    null, "图形验证码已过期，请重新输入");
                            return response;
                        }else{
                            String beforeCode = userForgetpwdRecord.getSms_validate_code();
                            Date beforeTIme = userForgetpwdRecord.getSms_validate_code_time();
                            if(!Strings.isNullOrEmpty(beforeCode)&&beforeTIme!=null){
                                calendar.setTime(beforeTIme);
                                calendar.add(Calendar.SECOND,60*5);
                                if(calendar.after(currCalendar)){
                                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "短信验证码有效期5分钟，请勿重复发送",
                                            null, "短信验证码有效期5分钟，请勿重复发送");
                                    return response;
                                }
                            }

                            Map<String, String> validateCode = customerUserService.getSmsValidateCode(userInfo.getUser_id(),phone_num);
                            if(validateCode!=null){
                                String resultValue = validateCode.get("result");
                                if("SUCCESS".equals(resultValue)){
                                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功",
                                            null, "获取成功");
                                    return response;
                                }else{
                                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, validateCode.get("message"),
                                            null, validateCode.get("message"));
                                    return response;
                                }
                            }
                        }
                    }
                }

            }else{
                if(!Strings.isNullOrEmpty(mobilePhone)&&mobilePhone.length()>4){
                    String alterPhoneNum = mobilePhone.substring(mobilePhone.length() - 5);
                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "手机号错误.请输入尾号为"+alterPhoneNum,
                            null, "手机号错误"+alterPhoneNum);
                    return response;
                }else{
                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "系统预留手机号有误，请联系管理员",
                            null, "系统预留手机号有误，请联系管理员");
                    return response;
                }

            }
        }else{
            JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "未找到该用户",
                    null, "未找到该用户");
            return response;
        }

        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功",
                null, userInfo);
        return response;
    }

    @RequestMapping("forgetPwd")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult forgetPwd(@RequestBody Map<String,Object> forgetPwdMap){
        String userName = (String) forgetPwdMap.get("user_name");
        String phone_num = (String) forgetPwdMap.get("phone_num");
        String validate_code = (String) forgetPwdMap.get("validate_code");
        String sms_validate_code = (String) forgetPwdMap.get("sms_validate_code");

        User userInfo = userService.getUserByUserNm(userName);

        if(userInfo!=null){
            String mobilePhone = userInfo.getMobile_phone();
            if(!Strings.isNullOrEmpty(mobilePhone)&&mobilePhone.equals(phone_num)){
                //检查用户输入验证码是否正确

                if(Strings.isNullOrEmpty(validate_code)){
                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "请输入图形验证码",
                            null, "请输入图形验证码");
                    return response;
                }

                UserForgetPwdRecord userForgetpwdRecord = customerUserService.getUserForgetPwdRecord(userInfo.getUser_id());
                if(userForgetpwdRecord==null){
                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "请输入图形验证码",
                            null, "请输入图形验证码");
                    return response;
                }else{
                    if(!userForgetpwdRecord.getValidate_code().equals(validate_code)){
                        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "图形验证码输入错误",
                                null, "图形验证码输入错误");
                        return response;
                    }else{
                        Date validateCodeTime = userForgetpwdRecord.getValidate_code_time();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(validateCodeTime);
                        calendar.add(Calendar.SECOND,60*5);
                        Calendar currCalendar = Calendar.getInstance();
                        currCalendar.setTime(new Date());
                        if(calendar.before(currCalendar)){
                            JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "图形验证码已过期，请重新输入",
                                    null, "图形验证码已过期，请重新输入");
                            return response;
                        }else{
                            String beforeCode = userForgetpwdRecord.getSms_validate_code();
                            Date beforeTIme = userForgetpwdRecord.getSms_validate_code_time();
                            if(!Strings.isNullOrEmpty(beforeCode)&&beforeTIme!=null){
                                calendar.setTime(beforeTIme);
                                calendar.add(Calendar.SECOND,60*5);
                                if(calendar.before(currCalendar)){
                                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "短信验证码过期，请重新获取",
                                            null, "短信验证码过期，请重新获取");
                                    return response;
                                }

                                if(!beforeCode.equals(sms_validate_code)){
                                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "短信验证码错误",
                                            null, "短信验证码错误");
                                    return response;
                                }

                                userInfo.setUser_status(String.valueOf(UserStatus.PWD_EXPIRED.getStatus()));
                                userService.updateUser(userInfo);

                                SessionSupport.addUserToSession(userInfo);

                                JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功",
                                        null, null);
                                return response;

                            }else{
                                JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "请获取短信验证码",
                                        null, "请获取短信验证码");
                                return response;
                            }

                        }
                    }
                }

            }else{
                if(!Strings.isNullOrEmpty(mobilePhone)&&mobilePhone.length()>4){
                    String alterPhoneNum = mobilePhone.substring(mobilePhone.length() - 5);
                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "手机号错误.请输入尾号为"+alterPhoneNum,
                            null, "手机号错误"+alterPhoneNum);
                    return response;
                }else{
                    JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "系统预留手机号有误，请联系管理员",
                            null, "系统预留手机号有误，请联系管理员");
                    return response;
                }

            }
        }else{
            JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "未找到该用户",
                    null, "未找到该用户");
            return response;
        }

    }

    @RequestMapping("getAreaData")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getAreaData(String parentId){
        List<ChinaAreaCode> allChindAreaCode = customerUserService.getAreaData(parentId);

        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功",
                null, allChindAreaCode);
        return response;
    }

    @RequestMapping("getOriginNature")
    @ResponseBody
    @CrossOrigin(allowCredentials = "true")
    public JsonResult getOriginNature(){
        List<OriginNature> originNatureList = customerUserService.getOriginNature();

        JsonResult response = JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "获取成功",
                null, originNatureList);
        return response;
    }


    @RequestMapping("delUserByUserId")
    @ResponseBody
    @JsonpCallback
    @CrossOrigin(allowCredentials="true")
    @Transactional(rollbackFor = Exception.class)
    public String delUserByUserId(Integer user_id){
        userService.delUserById(user_id);
        originService.removeUserOrigin(user_id);
        JsonResult jsonResult = new JsonResult();
        jsonResult.setResult(JsonResult.RESULT.SUCCESS);
        jsonResult.setResult_msg("删除成功");

        return jsonResult.toString();
    }

    private void changeUserInfo(Map<String,Object> selectOriginMap,String userId){
        String user_name_cn = selectOriginMap.containsKey("user_name_cn")?(String)selectOriginMap.get("user_name_cn"):null;
        String mobile_phone = selectOriginMap.containsKey("mobile_phone")?(String)selectOriginMap.get("mobile_phone"):null;
        String office_phone = selectOriginMap.containsKey("office_phone")?(String)selectOriginMap.get("office_phone"):null;
        String email = selectOriginMap.containsKey("email")?(String)selectOriginMap.get("email"):null;
        String social_code = selectOriginMap.containsKey("social_code")?(String)selectOriginMap.get("social_code"):null;
        User userFromDb = userService.getUserByUserId(new Integer(userId));
        userFromDb.setUser_name_cn(user_name_cn);
        userFromDb.setMobile_phone(mobile_phone);
        userFromDb.setOffice_phone(office_phone);
        userFromDb.setEmail(email);
        userFromDb.setSocial_code(social_code);
        userService.updateUser(userFromDb);

        SessionSupport.addUserToSession(userFromDb);
    }
}