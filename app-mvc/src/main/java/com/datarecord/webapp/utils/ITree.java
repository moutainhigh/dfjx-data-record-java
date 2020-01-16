package com.datarecord.webapp.utils;

import java.util.List;

/**
 * 树形数据实体接口
 * Modify By weiyy on 2019/08/11  （antdesign树的格式）
 * @param <T>
 */
public interface ITree<T> {
	
     String getValue();
	
     String getParentId();
    
     void setChildren(List<T> childList);
    
     List<T> getChildren();
}
