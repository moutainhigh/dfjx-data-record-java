package com.datarecord.webapp.utils;

import java.util.List;

/**
 * 生成树数据的接收类
 * Modify By weiyy on 2019/08/11  （antdesign树的格式）
 *
 */
public class EntityTree  {

	private String id;
	
	private String pId;
	
	private String name;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
