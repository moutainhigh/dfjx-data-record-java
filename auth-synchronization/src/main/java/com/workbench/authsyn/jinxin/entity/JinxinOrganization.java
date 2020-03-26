package com.workbench.authsyn.jinxin.entity;

import java.math.BigInteger;
import java.util.List;

public class JinxinOrganization {
    private BigInteger organizationId;
    private BigInteger parentId;
    private Integer level;
    private String organizationName;
    private String organizationCode;
    private String organizationNumber;
    private String organizationAddress;
    private String username;
    private String managerName;
    private BigInteger managerUserId;
    private String managerPhone;
    private String managerMail;
    private String userCount;
    private List<JinxinUser> oauthUsers;

    public BigInteger getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(BigInteger organizationId) {
        this.organizationId = organizationId;
    }

    public BigInteger getParentId() {
        return parentId;
    }

    public void setParentId(BigInteger parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationNumber() {
        return organizationNumber;
    }

    public void setOrganizationNumber(String organizationNumber) {
        this.organizationNumber = organizationNumber;
    }

    public String getOrganizationAddress() {
        return organizationAddress;
    }

    public void setOrganizationAddress(String organizationAddress) {
        this.organizationAddress = organizationAddress;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public BigInteger getManagerUserId() {
        return managerUserId;
    }

    public void setManagerUserId(BigInteger managerUserId) {
        this.managerUserId = managerUserId;
    }

    public String getManagerPhone() {
        return managerPhone;
    }

    public void setManagerPhone(String managerPhone) {
        this.managerPhone = managerPhone;
    }

    public String getManagerMail() {
        return managerMail;
    }

    public void setManagerMail(String managerMail) {
        this.managerMail = managerMail;
    }

    public String getUserCount() {
        return userCount;
    }

    public void setUserCount(String userCount) {
        this.userCount = userCount;
    }

    public List<JinxinUser> getOauthUsers() {
        return oauthUsers;
    }

    public void setOauthUsers(List<JinxinUser> oauthUsers) {
        this.oauthUsers = oauthUsers;
    }
}
