package com.workbench.shiro;

import com.google.common.base.Strings;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.jsonp.JsonResult;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * Shiro FIlter
 */
public class WorkbenchShiroFilter extends AuthenticatingFilter {

    private Logger logger = LoggerFactory.getLogger(WorkbenchShiroFilter.class);

    @Override
    /**
     * 该方法中放的是需要放行的请求
     * super.isAllowded中为：放行有权限的请求||放行登陆url||放行特殊配置的请求
     */
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {

//        return true;
        //放行OPTIONS请求
        //放行OPTIONS请求
        if(((HttpServletRequest) request).getMethod().equals(RequestMethod.OPTIONS.name())){
            return true;
        }

        return super.isAccessAllowed(request, response, mappedValue);
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse)  {
        logger.debug("create token is running.....");
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        String token = httpRequest.getHeader("token");

        //如果header中不存在token，则从参数中获取token
        if(Strings.isNullOrEmpty(token)){
            token = httpRequest.getParameter("token");
        }

        if(Strings.isNullOrEmpty(token)){
            return null;
        }

        WorkbenchShiroToken workbenchShiroToken = new WorkbenchShiroToken(token);

        return workbenchShiroToken;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        logger.debug("用户登陆校验失败");
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        StringBuilder builder = new StringBuilder();
        Enumeration<String> headerNames = httpRequest.getHeaderNames();
        builder.append("\n");
        while (headerNames.hasMoreElements()){
            String headerName = headerNames.nextElement();
            builder.append(headerName).append(":").append(httpRequest.getHeader(headerName)).append("\n");
        }

        builder.append("Request URL:").append(httpRequest.getRequestURI());
        logger.info("{}",builder.toString());

        String tokenHeader = httpRequest.getHeader("token");
        if(Strings.isNullOrEmpty(tokenHeader)){
            String originHost = httpRequest.getHeader("Origin");
            this.responseLoginFailed(httpResponse,originHost);
            return  false;
        }else{

        }
        
//        httpResponse.setStatus(200);

        //allow cors 允许跨域请求


        return false;
    }

    private void responseLoginFailed(HttpServletResponse httpResponse,String originHost) throws IOException {

        httpResponse.setHeader("Access-Control-Allow-Origin",originHost);
        httpResponse.setHeader("Access-Control-Allow-Methods","POST, GET, OPTIONS, PUT, DELETE");
        httpResponse.setHeader("Access-Control-Allow-Headers","X-PINGOTHER, Content-Type");
        httpResponse.setHeader("Access-Control-Allow-Credentials","true");

        //返回报文 包含重定向地址和状态码
        ServletOutputStream responseOutStream = httpResponse.getOutputStream();

        String responseJson = JsonSupport.makeJsonResultStr(JsonResult.RESULT.FAILD,"权限验证失败,需要重新登陆","ACCESS_DENIED","ACCESS_DENIED");

        responseOutStream.write(responseJson.getBytes());
    }
}
