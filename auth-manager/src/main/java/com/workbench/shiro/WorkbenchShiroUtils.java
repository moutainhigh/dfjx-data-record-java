package com.workbench.shiro;

import com.workbench.auth.user.entity.User;
import org.apache.shiro.SecurityUtils;

public final class WorkbenchShiroUtils {

    public static User checkUserFromShiroContext(){
        return (User) SecurityUtils.getSubject().getPrincipal();
    }
}
