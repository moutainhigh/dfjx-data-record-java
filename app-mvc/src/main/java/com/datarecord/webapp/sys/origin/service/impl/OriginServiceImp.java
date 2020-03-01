package com.datarecord.webapp.sys.origin.service.impl;

import com.datarecord.webapp.sys.origin.dao.IOriginDao;
import com.datarecord.webapp.sys.origin.entity.Origin;
import com.datarecord.webapp.sys.origin.service.OriginService;
import com.github.pagehelper.Page;
import com.webapp.support.page.PageResult;
import com.workbench.auth.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("origin")
public class OriginServiceImp implements OriginService {

    private Logger logger = LoggerFactory.getLogger(OriginServiceImp.class);

    @Autowired
    private IOriginDao originDao;

    @Override
    public List<Origin> listAllOrigin() {
        return originDao.listAllOrigin();
    }

    @Override
    public PageResult listOrigin(int currPage, int pageSize) {
        Page<Origin> originPage = originDao.listOrigin(currPage, pageSize);
        PageResult pageResult = PageResult.pageHelperList2PageResult(originPage);
        return pageResult;
    }

    public void createOrigin(Origin origin){
        originDao.createOrigin(origin);
    }

    @Override
    public Map<String, Object> getOriginById(Integer origin_id) {
        return originDao.getOriginById(origin_id);
    }

    @Override
    public void userOriginSave(Integer originId, Integer userId) {
        Origin originObj = originDao.getOriginByUserId(userId);
        if(originObj!=null){
            originObj.setOrigin_id(originId);
            originDao.userOriginUpdate(originId,userId);
        }else{
            originDao.removeUserOrigin(userId);
            originDao.userOriginSave(originId,userId);
        }
    }

    @Override
    public Origin getOriginByUser(Integer userId) {
        return originDao.getOriginByUserId(userId);
    }


    @Override
    public List<Origin> checkAllChildren(Integer originId){
        List<Origin> allOriginList = originDao.listAllOrigin();
        List<Origin> allSons = checkoutSons(originId, allOriginList);
        return allSons;
    }

    @Override
    public List<Origin> checkoutSons(Integer parentOriginId,List<Origin> originList){
        List<Origin> sons = new ArrayList();
        for (Origin origin : originList) {
            Integer parentId = origin.getParent_origin_id();
            if(parentOriginId.equals(parentId)){
                sons.add(origin);
                sons.addAll(this.checkoutSons(origin.getOrigin_id(),originList));
            }
        }
        return sons;
    }

    public Map<String,Origin> getFist2Origin(Integer checkOriginId,List<Origin> allOrigins){
//        logger.debug("getFist2Origin:{}",checkOriginId);
        Origin cityOrigin = null;
        Origin provinceOrigin = null;

        Map<Integer,Origin> originTmp = new HashMap<>();
        for (Origin allOrigin : allOrigins) {
            originTmp.put(allOrigin.getOrigin_id(),allOrigin);
        }

        Origin checkOrigin = originTmp.get(checkOriginId);

        Integer parentOriginId = checkOrigin.getParent_origin_id();


        Map<String,Origin> resultOrigin = new HashMap<>();
        resultOrigin.put("cityOrigin",null);
        resultOrigin.put("provinceOrigin",null);

        if(originTmp.containsKey(parentOriginId)){
            Origin parentOrigin = originTmp.get(parentOriginId);
            if(parentOrigin.getOrigin_id()==1){//判断当前检查的机构是否为省级机构
                resultOrigin.put("cityOrigin",null);
                resultOrigin.put("provinceOrigin",checkOrigin);
                return resultOrigin;
            }

            if(parentOrigin.getParent_origin_id()==1){//判断当前检查的机构是否为市级机构
                resultOrigin.put("cityOrigin",checkOrigin);
                resultOrigin.put("provinceOrigin",parentOrigin);
                return resultOrigin;
            }
        }else{//无上级机构信息，当球按机构为全国
            return resultOrigin;
        }

        while(true){
            cityOrigin = originTmp.get(parentOriginId);
            provinceOrigin = originTmp.get(cityOrigin.getParent_origin_id());
//            logger.debug("city origin {},{}",cityOrigin.getOrigin_id(),cityOrigin.getParent_origin_id());
            if(provinceOrigin.getParent_origin_id()==1){
                break;
            }
            parentOriginId = cityOrigin.getParent_origin_id();
        }

        resultOrigin.put("cityOrigin",cityOrigin);
        resultOrigin.put("provinceOrigin",provinceOrigin);

        return resultOrigin;


    }

    public Collection<Map<String, Object>> checkProvAndCity(List<Origin> allOrigins){

        Map<Integer,Map<String,Object>> provinceOriginTmp = new HashMap<>();

        for (Origin allOrigin : allOrigins) {
            if(allOrigin.getParent_origin_id()==1){
                Map<String,Object> tmp = new HashMap<>();
                tmp.put("province",allOrigin);
                tmp.put("citys",new ArrayList<Origin>());
                provinceOriginTmp.put(allOrigin.getOrigin_id(),tmp);
            }
        }

        for (Origin allOrigin : allOrigins) {
            Integer parentOriId = allOrigin.getParent_origin_id();
            if(provinceOriginTmp.containsKey(parentOriId)){
                ArrayList<Origin> cityList = (ArrayList<Origin>) provinceOriginTmp.get(parentOriId).get("citys");
                cityList.add(allOrigin);
            }
        }

        return provinceOriginTmp.values();


    }

    @Override
    public List<Origin> getOriginByName(String searchOriginName) {
        List<Origin> result = originDao.getOriginByName(searchOriginName);
        return result;
    }

    public Origin getOriginTree(List<Integer> childrenOrigins, List<Origin> allOrigins){
        Map<Integer,Origin> fullOriginTMp = new HashMap<>();
        if(allOrigins!=null&&allOrigins.size()>0){
            for (Origin originTmp : allOrigins) {
                Integer originId = originTmp.getOrigin_id();
                fullOriginTMp.put(originId,originTmp);
            }
        }

        Map<Integer,Origin> checkoutOriginMap = new HashMap<>();

        if(childrenOrigins!=null){
            for (Integer originChild : childrenOrigins) {
                checkoutOriginMap.putAll(this.getParentOrigin(fullOriginTMp,originChild));
            }
        }

        logger.debug("{}",checkoutOriginMap);

        Origin originTree = makeOriginTree(checkoutOriginMap);

        return originTree;
    }

    @Override
    public List<User> getUsersByOrigin(Integer originId) {
        List<User> users = originDao.getUsersByOrigin(originId);
        return users;
    }

    @Override
    public void updateOrigin(Origin origin) {
        originDao.updateOrigin(origin);
    }

    @Override
    public void removeUserOrigin(int userId) {
        originDao.removeUserOrigin(userId);
    }

    @Override
    public Origin getAllOriginTree() {
        List<Origin> allOrigin = originDao.listAllOrigin();
        Map<Integer,Origin> originTmp = new HashMap<>();
        for (Origin origin : allOrigin) {
            originTmp.put(origin.getOrigin_id(),origin);
        }

        Origin origin = this.makeOriginTree(originTmp);
        return origin;
    }


    public Origin makeOriginTree(Map<Integer,Origin> originsMap){
        Set<Integer> originIds = originsMap.keySet();
        Integer rootOrigin = null;
        for (Integer originId : originIds) {
            Origin origin = originsMap.get(originId);
            Integer parentOriginId = origin.getParent_origin_id();
            if(originsMap.containsKey(parentOriginId)){
                List<Origin> allChildren = originsMap.get(parentOriginId).getChildren();
                if(allChildren==null){
                    allChildren = new ArrayList<>();
                    originsMap.get(parentOriginId).setChildren(allChildren);
                }

                originsMap.get(parentOriginId).getChildren().add(origin);
            }else{
                rootOrigin = origin.getOrigin_id();
            }
        }

        return originsMap.get(rootOrigin);
    }

    private Map<Integer, Origin> getParentOrigin(Map<Integer,Origin> fullOriginTMp, Integer checkOriginId){
        logger.debug("获取上级机构 {}",checkOriginId);
        Map<Integer,Origin> resultOrigins = new HashMap<>();
        if(fullOriginTMp.containsKey(checkOriginId)){
            Origin checkOrigin = fullOriginTMp.get(checkOriginId);
            resultOrigins.put(checkOriginId,checkOrigin);
            Integer parentOrigin = checkOrigin.getParent_origin_id();
            if(parentOrigin!=null){
                resultOrigins.putAll(this.getParentOrigin(fullOriginTMp,parentOrigin));
            }

        }

        return resultOrigins;
    }
}
