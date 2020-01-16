package com.datarecord.webapp.utils;

import java.util.List;

/**
 * 生成树数据的接收类
 * Modify By weiyy on 2019/08/11  （antdesign树的格式）
 *
 */
public class EntityTree implements ITree<EntityTree> {

	private String value;
	
	private String parentId;
	
	private String title;
	
	private List<EntityTree> children;

	@Override
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public List<EntityTree> getChildren() {
		return children;
	}

	@Override
	public void setChildren(List<EntityTree> children) {
		this.children = children;
	}
}
