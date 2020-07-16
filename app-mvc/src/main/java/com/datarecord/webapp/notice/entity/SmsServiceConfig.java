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

    @Value("${sms.service.wsdlUrl}")
    private String wsdlUrl;

    @Value("${sms.service.user}")
    private String user;

    @Value("${sms.service.password}")
    private String password;

    @Value("${sms.service.clientcode}")
    private String clientcode;

    @Value("${sms.service.timeout}")
    private String timeout;

    @Value("${sms.query.host}")
    private String smsServiceHost;
    @Value("${sms.query.port}")
    private String smsServicPort;
    @Value("${sms.query.appname}")
    private String smsServicAppName;
    @Value("${sms.service.wsdlUrl}")
    private String smsServicSendurl;
    @Value("${sms.query.sendResultUrl}")
    private String smsServicSendResultUrl;
    @Value("${sms.query.queryurl}")
    private String queryurl;

    public String toString(){
        return new StringBuilder().append("{").
                append("host:").append(this.host).
                append(",port:").append(this.port).
                append(",appname:").append(this.appname).
                append(",wsdlUrl:").append(this.wsdlUrl).
                append(",user:").append(this.user).
                append(",password:").append(this.password).
                append(",clientcode:").append(this.clientcode).
                append(",timeout:").append(this.timeout).
                append(",smsServiceHost:").append(this.smsServiceHost).
                append(",queryApsmsServicPortpName:").append(this.smsServicPort).
                append(",smsServicAppName:").append(this.smsServicAppName).
                append(",smsServicSendurl:").append(this.smsServicSendurl).
                append(",smsServicSendResultUrl:").append(this.smsServicSendResultUrl).
                append(",queryurl:").append(this.queryurl).
                append("}").toString();
    }

    public String getSmsServiceHost() {
        return smsServiceHost;
    }

    public void setSmsServiceHost(String smsServiceHost) {
        this.smsServiceHost = smsServiceHost;
    }

    public String getSmsServicPort() {
        return smsServicPort;
    }

    public void setSmsServicPort(String smsServicPort) {
        this.smsServicPort = smsServicPort;
    }

    public String getSmsServicAppName() {
        return smsServicAppName;
    }

    public void setSmsServicAppName(String smsServicAppName) {
        this.smsServicAppName = smsServicAppName;
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

    public String getSmsServicSendurl() {
        return smsServicSendurl;
    }

    public void setSmsServicSendurl(String smsServicSendurl) {
        this.smsServicSendurl = smsServicSendurl;
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

    public String getWsdlUrl() {
        return wsdlUrl;
    }

    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    public String getSmsServicSendResultUrl() {
        return smsServicSendResultUrl;
    }

    public void setSmsServicSendResultUrl(String smsServicSendResultUrl) {
        this.smsServicSendResultUrl = smsServicSendResultUrl;
    }
}
