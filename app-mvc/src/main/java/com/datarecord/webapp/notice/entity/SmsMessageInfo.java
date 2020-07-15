package com.datarecord.webapp.notice.entity;

public class SmsMessageInfo {
	private String id;//32位码
	private String smsid; //36位码
	private String sendnum;//标识
	private String recvnum;//接收电话号码
	private String content;//内容
	private String recvtime;//发送时间

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSmsid() {
		return smsid;
	}
	public void setSmsid(String smsid) {
		this.smsid = smsid;
	}
	public String getSendnum() {
		return sendnum;
	}
	public void setSendnum(String sendnum) {
		this.sendnum = sendnum;
	}
	public String getRecvnum() {
		return recvnum;
	}
	public void setRecvnum(String recvnum) {
		this.recvnum = recvnum;
	}
	public String getRecvtime() {
		return recvtime;
	}
	public void setRecvtime(String recvtime) {
		this.recvtime = recvtime;
	}
	@Override
	public String toString() {
		return "MessageInfo [id=" + id + ", smsid=" + smsid + ", sendnum="
				+ sendnum + ", recvnum=" + recvnum + ", content=" + content
				+ ", recvtime=" + recvtime  + "]";
	}
	
	
	
}