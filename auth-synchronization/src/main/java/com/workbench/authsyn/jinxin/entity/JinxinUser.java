package com.workbench.authsyn.jinxin.entity;

import java.math.BigInteger;
import java.util.List;

public class JinxinUser {
    private BigInteger userId;
    private BigInteger tenantId;
    private String name;
    private String username;
    private Object userInfoVOS;
    private String password;
    private String accessToken;
    private String refreshToken;
    private String phoneNumber;
    private String tenantName;
    private String virtualCurrency;
    private String email;
    private String fixedPhoneNumber;
    private String faxNumber;
    private String jobNumber;
    private Boolean enabled;
    private Integer userSex;
    private String userBirthday;
    private String position;
    private String title;
    private String personalSignature;
    private String userAvatar;
    private String userAddress;
    private String outsourcingCompany;
    private String outsourcingPhone;
    private Integer roleId;
    private String roleName;
    private String tenantLevel;
    private Boolean systemUser;
    private Object lastLoginTime;
    private Object permissionCodes;
    private Boolean tenantManager;
    private Object appKeys;

    private List<JinxinRole> roles;
    private Object groups;
    private List<JinxinOrganization> organizations;

    public BigInteger getUserId() {
        return userId;
    }

    public void setUserId(BigInteger userId) {
        this.userId = userId;
    }

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getVirtualCurrency() {
        return virtualCurrency;
    }

    public void setVirtualCurrency(String virtualCurrency) {
        this.virtualCurrency = virtualCurrency;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFixedPhoneNumber() {
        return fixedPhoneNumber;
    }

    public void setFixedPhoneNumber(String fixedPhoneNumber) {
        this.fixedPhoneNumber = fixedPhoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public String getJobNumber() {
        return jobNumber;
    }

    public void setJobNumber(String jobNumber) {
        this.jobNumber = jobNumber;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getUserSex() {
        return userSex;
    }

    public void setUserSex(Integer userSex) {
        this.userSex = userSex;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPersonalSignature() {
        return personalSignature;
    }

    public void setPersonalSignature(String personalSignature) {
        this.personalSignature = personalSignature;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getOutsourcingCompany() {
        return outsourcingCompany;
    }

    public void setOutsourcingCompany(String outsourcingCompany) {
        this.outsourcingCompany = outsourcingCompany;
    }

    public String getOutsourcingPhone() {
        return outsourcingPhone;
    }

    public void setOutsourcingPhone(String outsourcingPhone) {
        this.outsourcingPhone = outsourcingPhone;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTenantLevel() {
        return tenantLevel;
    }

    public void setTenantLevel(String tenantLevel) {
        this.tenantLevel = tenantLevel;
    }

    public Boolean getSystemUser() {
        return systemUser;
    }

    public void setSystemUser(Boolean systemUser) {
        this.systemUser = systemUser;
    }

    public Object getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Object lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Object getPermissionCodes() {
        return permissionCodes;
    }

    public void setPermissionCodes(Object permissionCodes) {
        this.permissionCodes = permissionCodes;
    }

    public Boolean getTenantManager() {
        return tenantManager;
    }

    public void setTenantManager(Boolean tenantManager) {
        this.tenantManager = tenantManager;
    }

    public Object getAppKeys() {
        return appKeys;
    }

    public void setAppKeys(Object appKeys) {
        this.appKeys = appKeys;
    }

    public List<JinxinRole> getRoles() {
        return roles;
    }

    public void setRoles(List<JinxinRole> roles) {
        this.roles = roles;
    }

    public Object getGroups() {
        return groups;
    }

    public void setGroups(Object groups) {
        this.groups = groups;
    }

    public List<JinxinOrganization> getOrganizations() {
        return organizations;
    }

    public void setOrganizations(List<JinxinOrganization> organizations) {
        this.organizations = organizations;
    }


    public Object getUserInfoVOS() {
        return userInfoVOS;
    }

    public void setUserInfoVOS(Object userInfoVOS) {
        this.userInfoVOS = userInfoVOS;
    }
}
