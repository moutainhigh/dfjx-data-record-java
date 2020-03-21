package com.datarecord.webapp.utils;

import com.workbench.auth.user.entity.User;
import com.workbench.shiro.WorkbenchShiroUtils;

public class DataRecordUtil {
    
    public static boolean isSuperUser(){
        User user = WorkbenchShiroUtils.checkUserFromShiroContext();
        if(user!=null){
            if("3".equals(user.getUser_type())){
                return true;
            }
        }
        return false;
    }
}
