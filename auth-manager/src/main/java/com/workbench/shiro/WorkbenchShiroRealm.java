package com.workbench.shiro;

import com.workbench.auth.user.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Shiro验证自实现
 */
public class WorkbenchShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(WorkbenchShiroRealm.class);

    @Autowired
    private UserService userService;

    /**
     * 获取登陆用户的所有权限
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        logger.debug("do get authorization info is running.....");
        SimpleAuthorizationInfo authenticationInfo = new SimpleAuthorizationInfo();
        return authenticationInfo;
    }

    /**
     * 登陆验证方法
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(authenticationToken.getPrincipal(),authenticationToken.getCredentials(),this.getName());
        return authenticationInfo;
    }
}
