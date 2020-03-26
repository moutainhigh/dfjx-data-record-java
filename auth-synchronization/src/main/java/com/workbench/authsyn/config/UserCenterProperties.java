package com.workbench.authsyn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:user-center.properties")
public class UserCenterProperties {
    @Value("${center.enable}")
    private Boolean enable;

    @Value("${center.eachTime}")
    private Integer eachTime;

    @Value("${center.host}")
    private String centerHost;

    @Value("${center.port}")
    private String centerPort;

    @Value("${center.appname}")
    private String centerAppName;

    @Value("${center.username}")
    private String centerUserName;

    @Value("${center.password}")
    private String centerUserPassword;

    public String getCenterHost() {
        return centerHost;
    }

    public void setCenterHost(String centerHost) {
        this.centerHost = centerHost;
    }

    public String getCenterPort() {
        return centerPort;
    }

    public void setCenterPort(String centerPort) {
        this.centerPort = centerPort;
    }

    public String getCenterAppName() {
        return centerAppName;
    }

    public void setCenterAppName(String centerAppName) {
        this.centerAppName = centerAppName;
    }

    public String getCenterUserName() {
        return centerUserName;
    }

    public void setCenterUserName(String centerUserName) {
        this.centerUserName = centerUserName;
    }

    public String getCenterUserPassword() {
        return centerUserPassword;
    }

    public void setCenterUserPassword(String centerUserPassword) {
        this.centerUserPassword = centerUserPassword;
    }

    public Boolean getEnable() {
        return enable;
    }

    public void setEnable(Boolean enable) {
        this.enable = enable;
    }

    public Integer getEachTime() {
        return eachTime;
    }

    public void setEachTime(Integer eachTime) {
        this.eachTime = eachTime;
    }
}
