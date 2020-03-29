package com.workbench.shiro;

import com.workbench.auth.user.entity.User;
import org.apache.shiro.SecurityUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class WorkbenchShiroUtils {

    public static User checkUserFromShiroContext(){
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    public static void updateShiroUser(User user) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        User userFromShiro = checkUserFromShiroContext();

        Class<User> userClazz = User.class;

        Method[] methods = userClazz.getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            if(methodName.startsWith("set")){
                String filedName = methodName.replace("set", "");
                Method getMethod = userClazz.getMethod(new StringBuilder().append("get").append(filedName).toString());
                method.invoke(userFromShiro,getMethod.invoke(user));
            }
        }
    }
}
