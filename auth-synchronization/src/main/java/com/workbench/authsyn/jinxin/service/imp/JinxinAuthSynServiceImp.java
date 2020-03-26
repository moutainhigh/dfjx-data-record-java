package com.workbench.authsyn.jinxin.service.imp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.webapp.support.encryption.MD5;
import com.webapp.support.httpClient.HttpClientSupport;
import com.webapp.support.json.JsonSupport;
import com.webapp.support.page.PageResult;
import com.workbench.auth.menu.entity.Menu;
import com.workbench.auth.menu.service.MenuService;
import com.workbench.authsyn.config.UserCenterProperties;
import com.workbench.authsyn.dao.IUserSynDao;
import com.workbench.authsyn.jinxin.entity.JinxinOrganization;
import com.workbench.authsyn.jinxin.entity.JinxinUser;
import com.workbench.authsyn.jinxin.service.JinxinAuthSynService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.util.*;

@Service("JinxinUserSynService")
public class JinxinAuthSynServiceImp implements JinxinAuthSynService {

    private Logger logger = LoggerFactory.getLogger(JinxinAuthSynServiceImp.class);

    @Autowired
    private UserCenterProperties userCenterProperties;

    @Autowired
    private IUserSynDao userSynDao;
    
    @Autowired
    private MenuService menuService;

    private static final Integer BATCH_USER_LIMIT = 100;

    public List<JinxinUser> getUsersListFromJinxinUserCenter() throws IOException, URISyntaxException {
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("offset",1);
        paramMap.put("limit",BATCH_USER_LIMIT);
        String response = this.getUserList(1, BATCH_USER_LIMIT);
        Map responseMap = JsonSupport.jsonToMap(response);
        Object userData = responseMap.get("data");
        Integer totalUserCount = (Integer) responseMap.get("total");

        List<JinxinUser> jinxinUsers = parseUserList(userData);
        if(totalUserCount>BATCH_USER_LIMIT){
            int forCount = totalUserCount / BATCH_USER_LIMIT;
            if(totalUserCount % BATCH_USER_LIMIT>0){
                forCount++;
            }
            for(int offserVal=2;offserVal<=forCount;offserVal++){
                response = this.getUserList(offserVal, BATCH_USER_LIMIT);
                responseMap = JsonSupport.jsonToMap(response);
                userData = responseMap.get("data");
                jinxinUsers.addAll(parseUserList(userData));
            }
        }
        return jinxinUsers;
    }

    private List<JinxinUser> parseUserList(Object userData){
        ObjectMapper objectMapper = new ObjectMapper();
        List<JinxinUser> jinxinUsers = objectMapper.convertValue(userData,new TypeReference<List<JinxinUser>>() { });
        return jinxinUsers;
    }

    private String getUserList(Integer offset,Integer limit) throws IOException, URISyntaxException {
        HttpClientSupport httpClient = getJinxinUserCenter();
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("offset",offset);
        paramMap.put("limit",limit);
        String response = httpClient.sendRequest("sync/getAllUser", paramMap, RequestMethod.POST, true);

        return response;
    }

    private HttpClientSupport getJinxinUserCenter(){
        StringBuilder userCenterUrl = new StringBuilder()
                .append(userCenterProperties.getCenterHost())
                .append(Strings.isNullOrEmpty(userCenterProperties.getCenterPort())?"":":")
                .append(userCenterProperties.getCenterPort())
                .append(Strings.isNullOrEmpty(userCenterProperties.getCenterAppName())?"":"/")
                .append(userCenterProperties.getCenterAppName());
        HttpClientSupport httpClient = HttpClientSupport.getInstance(userCenterUrl.toString());
        return httpClient;
    }

    @Override
    public List<JinxinOrganization> getOriginFromJinxinUserCenter() {
        HttpClientSupport httpClient = getJinxinUserCenter();
        Map<String,Object> paramMap = new HashMap<>();
        try {
            String response = httpClient.sendRequest("sync/getAllOrganization", paramMap, RequestMethod.POST, true);

            List<JinxinOrganization> jinxinOrigins = (List<JinxinOrganization>) JsonSupport.jsonToList4Generic(response, ArrayList.class,JinxinOrganization.class);

//            ObjectMapper objectMapper = new ObjectMapper();
//            List<JinxinOrganization> jinxinOrigins = objectMapper.convertValue(response,new TypeReference<List<JinxinOrganization>>() { });

            return jinxinOrigins;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void trancatUsers() {
        userSynDao.trancatUserTable();
    }

    @Override
    public void saveUser(JinxinUser jinxinUser) {
        jinxinUser.setPassword(MD5.getMD5Value("123456"));
        userSynDao.saveUser(jinxinUser);
    }

    @Override
    public void trancatOrigins() {
        userSynDao.trancatOriginTable();
    }

    @Override
    public void saveOrigins(JinxinOrganization jinxinOrganization) {
        userSynDao.saveOrigins(jinxinOrganization);
    }

    @Override
    public void trancatUserOrigins() {
        userSynDao.trancatUserOrigins();
    }

    @Override
    public void saveUserOrigin(BigInteger userId, BigInteger organizationId) {
        userSynDao.saveUserOrigin(userId,organizationId);
    }

    @Override
    public List<Menu> getUserAuths(BigInteger user_id) throws IOException, URISyntaxException {
        HttpClientSupport jinxinUserCenter = this.getJinxinUserCenter();
        Map<String,Object> params = new HashMap<>();
        params.put("userId",user_id);
        params.put("appName","DATAREPORT");
        String responseStr = jinxinUserCenter.sendRequest(
                "role/get/permission/userid/system",
                params,
                RequestMethod.GET,
                false
        );


        PageResult allMenu = menuService.listMenuByPage(1, Integer.MAX_VALUE);
        List<Menu> menus = allMenu.getDataList();
        List<Menu> menuResultList = new ArrayList<>();
        List<Integer> unCheckedParentMenu = new ArrayList<>();
        Map authMap = JsonSupport.jsonToMap(responseStr);
        if(authMap!=null&&authMap.size()>0){
            Collection allMenuId = authMap.values();
            for (Menu menu : menus) {
                int moduleId = menu.getModule_id();
                if(allMenuId.contains(String.valueOf(moduleId))){
                    menuResultList.add(menu);
                    Integer superModuleId = menu.getSuper_module_id();
                    if(!unCheckedParentMenu.contains(superModuleId)&&!allMenuId.contains(String.valueOf(superModuleId))){
                        unCheckedParentMenu.add(superModuleId);
                    }
                }
                if(unCheckedParentMenu.contains(moduleId)&&!menuResultList.contains(menu)){
                    menuResultList.add(menu);
                    unCheckedParentMenu.remove(moduleId);
                }
            }
            for (Menu menu : menus) {
                Integer moduleId = menu.getModule_id();
                if(unCheckedParentMenu.contains(moduleId)){
                    menuResultList.add(menu);
                }
            }
        }
        return menuResultList;
    }
}
