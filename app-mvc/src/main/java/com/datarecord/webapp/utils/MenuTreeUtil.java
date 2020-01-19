package com.datarecord.webapp.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成树数据的工具类
 * Modify By ppj on 2020/01/19
 *
 */
public class MenuTreeUtil {
    public static Map<String,Object> mapArray = new LinkedHashMap<String, Object>();
    public List<EntityTree> menuCommon;
    public List<Object> list = new ArrayList<Object>();

    public List<Object> menuList(List<EntityTree> menu,String orgId){
        this.menuCommon = menu;
        for (EntityTree x : menu) {
            Map<String,Object> mapArr = new LinkedHashMap<String, Object>();
            if(x.getpId().equals(orgId)){
                mapArr.put("id", x.getId());
                mapArr.put("name", x.getName());
                mapArr.put("pid", x.getpId());
                mapArr.put("childList", menuChild(x.getId()));
                list.add(mapArr);
            }
        }
        return list;
    }

    public List<?> menuChild(String id){
        List<Object> lists = new ArrayList<Object>();
        for(EntityTree a:menuCommon){
            Map<String,Object> childArray = new LinkedHashMap<String, Object>();
            if(a.getpId().equals(id)){
                childArray.put("id", a.getId());
                childArray.put("name", a.getName());
                childArray.put("pid", a.getpId());
                childArray.put("childList", menuChild(a.getId()));
                lists.add(childArray);
            }
        }
        return lists;
    }

}
