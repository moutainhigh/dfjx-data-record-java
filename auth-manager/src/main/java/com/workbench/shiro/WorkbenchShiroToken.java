package com.workbench.shiro;

import com.workbench.auth.user.entity.User;
import org.apache.shiro.authc.AuthenticationToken;

public class WorkbenchShiroToken implements AuthenticationToken {

    private User principal;

    private String credentials;

    public WorkbenchShiroToken(String token){
        this.principal = new User();
        this.credentials = token;
    }

    public WorkbenchShiroToken(User userEntity,String token){
        this.principal = userEntity;
        this.credentials = token;
    }

    @Override
    public User getPrincipal() {
        return this.principal;
    }

    @Override
    public String getCredentials() {
        return this.credentials;
    }
}
