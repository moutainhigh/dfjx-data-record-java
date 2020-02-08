package com.datarecord.webapp.utils;

import java.util.List;

/**
 * 生成树数据的接收类
 * Modify By ppj on 2020/01/19
 *
 */
public class EntityTree  {

	private String id;
	
	private String pId;
	
	private String name;

	private String user_id;

	private String user_name;


	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

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
