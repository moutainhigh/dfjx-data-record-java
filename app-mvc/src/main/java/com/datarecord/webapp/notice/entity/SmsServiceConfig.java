package com.datarecord.webapp.notice.entity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:sms-center.properties")
public class SmsServiceConfig {

    @Value("${sms.service.host}")
    private String host;

    @Value("${sms.service.port}")
    private String port;

    @Value("${sms.service.appname}")
    private String appname;

    @Value("${sms.service.sendurl}")
    private String sendurl;

    @Value("${sms.service.user}")
    private String user;

    @Value("${sms.service.password}")
    private String password;

    @Value("${sms.service.clientcode}")
    private String clientcode;

    @Value("${sms.service.timeout}")
    private String timeout;

    @Value("${sms.query.host}")
    private String queryHost;
    @Value("${sms.query.port}")
    private String queryPort;
    @Value("${sms.query.appname}")
    private String queryAppName;
    @Value("${sms.query.queryurl}")
    private String queryurl;

    public String toString(){
        return new StringBuilder().append("{").
                append("host:").append(this.host).
                append(",port:").append(this.port).
                append(",appname:").append(this.appname).
                append(",sendurl:").append(this.sendurl).
                append(",user:").append(this.user).
                append(",password:").append(this.password).
                append(",clientcode:").append(this.clientcode).
                append(",timeout:").append(this.timeout).
                append(",queryHost:").append(this.queryHost).
                append(",queryAppName:").append(this.queryPort).
                append(",host:").append(this.queryAppName).
                append(",queryurl:").append(this.queryurl).
                append("}").toString();
    }

    public String getQueryHost() {
        return queryHost;
    }

    public void setQueryHost(String queryHost) {
        this.queryHost = queryHost;
    }

    public String getQueryPort() {
        return queryPort;
    }

    public void setQueryPort(String queryPort) {
        this.queryPort = queryPort;
    }

    public String getQueryAppName() {
        return queryAppName;
    }

    public void setQueryAppName(String queryAppName) {
        this.queryAppName = queryAppName;
    }

    public String getQueryurl() {
        return queryurl;
    }

    public void setQueryurl(String queryurl) {
        this.queryurl = queryurl;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getAppname() {
        return appname;
    }

    public void setAppname(String appname) {
        this.appname = appname;
    }

    public String getSendurl() {
        return sendurl;
    }

    public void setSendurl(String sendurl) {
        this.sendurl = sendurl;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getClientcode() {
        return clientcode;
    }

    public void setClientcode(String clientcode) {
        this.clientcode = clientcode;
    }
}
