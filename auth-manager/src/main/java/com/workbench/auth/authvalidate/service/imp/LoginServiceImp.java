package com.workbench.auth.authvalidate.service.imp;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.webapp.support.encryption.MD5;
import com.workbench.auth.authvalidate.service.LoginService;
import com.workbench.auth.authvalidate.bean.LoginResult;
import com.workbench.auth.user.entity.UserStatus;
import com.workbench.auth.user.service.UserService;
import com.workbench.auth.user.entity.User;
import com.workbench.shiro.WorkbenchShiroToken;
import com.workbench.utils.TokenGenerator;
import org.apache.shiro.authc.AuthenticationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Created by pc on 2017/6/29.
 */

@Service("loginService")
public class LoginServiceImp implements LoginService{

    private static Logger logger = LoggerFactory.getLogger(LoginServiceImp.class);

    @Autowired
    private UserService userService;

    public LoginResult validate(String userNm, String password) {

        User checkResult = userService.checkUser(userNm, password);

        return loginCheck(checkResult);
//        return null;
    }

    public LoginResult validateMergePwd(String userNm){
        User checkResult = userService.getUserByUserNm(userNm);

        return loginCheck(checkResult);
    }

    @Override
    public String createToken(String userId) {

        String tokenValue = TokenGenerator.generateValue();
        AuthenticationToken tokenObj = new WorkbenchShiroToken(new User(),tokenValue);

//        SecurityUtils.getSubject().login(tokenObj);
        return tokenValue;
    }

    private LoginResult loginCheck(User checkResult){
        LoginResult loginResult = new LoginResult();
        if(checkResult!=null){
            String userStatus = checkResult.getUser_status();
            if(Strings.isNullOrEmpty(userStatus)){
                loginResult.setResult_code(LoginResult.LOGIN_RESULT.STATUS_FAIL);
                loginResult.setValidate_result("用户状态异常");
                return loginResult;
            }
            Integer statusInt = new Integer(userStatus);
            if(UserStatus.LOCK.equal(statusInt)){
                loginResult.setResult_code(LoginResult.LOGIN_RESULT.LOCK);
                loginResult.setValidate_result("用户被锁定");
            }else if(UserStatus.PWD_EXPIRED.equal(statusInt)){
                loginResult.setResult_code(LoginResult.LOGIN_RESULT.PWD_EXPIRED);
                loginResult.setValidate_result("用户密码过期");
            }else if(UserStatus.NEVER_LOGIN.equal(statusInt)){
                loginResult.setResult_code(LoginResult.LOGIN_RESULT.NEVER_LOGIN);
                loginResult.setValidate_result(UserStatus.NEVER_LOGIN.getStatus_cn());
                checkResult.setUser_status(String.valueOf(UserStatus.NOT_NOMAL_TAG.getStatus()));
                userService.updateUser(checkResult);
            }else if(UserStatus.NOT_NOMAL_TAG.equal(statusInt)){
                loginResult.setResult_code(LoginResult.LOGIN_RESULT.USER_STATS_NOT_NORMAL);
                loginResult.setValidate_result(UserStatus.NOT_NOMAL_TAG.getStatus_cn());
            }
            else{
                loginResult.setResult_code(LoginResult.LOGIN_RESULT.SUCCESS);
                loginResult.setValidate_result("验证通过");
            }


        }else{
            loginResult.setResult_code(LoginResult.LOGIN_RESULT.VALIDATE_FAIL);
            loginResult.setValidate_result("用户名或密码错误");
        }

        String validateResult = MoreObjects.toStringHelper(loginResult).add("result", loginResult.getResult_code()).
                add("result_msg", loginResult.getValidate_result()).toString();

        logger.debug("validate result {}",validateResult);
        return loginResult;
    }
}
