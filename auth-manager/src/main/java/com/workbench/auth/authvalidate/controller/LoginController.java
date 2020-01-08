package com.workbench.auth.authvalidate.controller;

import com.google.common.base.Strings;
import com.webapp.support.encryption.MD5;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import com.workbench.auth.authvalidate.service.LoginService;
import com.workbench.auth.authvalidate.bean.LoginResult;
import com.workbench.auth.user.entity.User;
import com.workbench.auth.user.service.UserService;
import com.workbench.shiro.WorkbenchShiroToken;
import com.workbench.spring.aop.annotation.JsonpCallback;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pc on 2017/6/30.
 */

@Controller
@RequestMapping("sys/login")
public class LoginController extends AbstractLoginController{

    private Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @RequestMapping(value="doLogin",method = {RequestMethod.GET,RequestMethod.POST})
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public JsonResult doLogin(String user_name, String user_pwd){
        boolean checkResult = Strings.isNullOrEmpty(user_name);

        Map<String,Object> loginResultData = new HashMap<String,Object>();

        User user = null;
        String tokenValue = null;
        if(checkResult){
            return JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, "用户名为空", LoginResult.LOGIN_RESULT.USERNM_NOT_NULL.toString(), null);

        }else{
            LoginResult loginResult = loginService.validate(user_name, user_pwd);
            if(loginResult.getResult_code()!=LoginResult.LOGIN_RESULT.SUCCESS){

                if(LoginResult.LOGIN_RESULT.PWD_EXPIRED.equals(loginResult.getResult_code())){
                    user = userService.getUserByUserNm(user_name);
                    tokenValue = loginService.createToken(String.valueOf(user.getUser_id()));
                    loginResultData.put("RESULT",LoginResult.LOGIN_RESULT.PWD_EXPIRED);
                    loginResultData.put("TOKEN",tokenValue);
                    return JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "登陆成功,密码过期需要修改",
                            null, loginResultData);
                }

                return JsonSupport.makeJsonpResult(JsonResult.RESULT.FAILD, loginResult.getValidate_result(),
                        loginResult.getResult_code().toString(), null);
            }else{
                user = userService.getUserByUserNm(user_name);
                tokenValue = loginService.createToken(String.valueOf(user.getUser_id()));
            }

        }
        loginResultData.put("RESULT",LoginResult.LOGIN_RESULT.SUCCESS);
        loginResultData.put("TOKEN",tokenValue);
        WorkbenchShiroToken token = new WorkbenchShiroToken(user,tokenValue);
        SecurityUtils.getSubject().login(token);

        return JsonSupport.makeJsonpResult(JsonResult.RESULT.SUCCESS, "登录成功",null, loginResultData);
    }

    @RequestMapping("checkLoginUser")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String checkLoginUser(){
        String responseJson = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS, "获取成功", null, this.getLoginUserInfo());
        return responseJson;
    }

    @RequestMapping("logout")
    @ResponseBody
    @CrossOrigin(allowCredentials="true")
    public String logout() {
        SecurityUtils.getSubject().logout();

        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        ServletContext requestContext = request.getServletContext();

        Boolean casSwitch = new Boolean(requestContext.getInitParameter("casSwitch"));
        logger.warn("cas swtich :{}",casSwitch);
        String responseJson = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS,"登出成功","SUCCESS",null);
        if(casSwitch){
            String potentialRedirectUrl = requestContext.getInitParameter("casLogoutRedirect");
            logger.warn("potentialRedirectUrl :{}",potentialRedirectUrl);
            responseJson = JsonSupport.makeJsonResultStr(JsonResult.RESULT.SUCCESS,"登出成功","FORWARD_CAS",potentialRedirectUrl);
        }
        return responseJson;
    }

}
