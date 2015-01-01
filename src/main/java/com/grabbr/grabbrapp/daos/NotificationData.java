package com.grabbr.grabbrapp.daos;

public class NotificationData {
	
	String Text;
	String userid;
	String userid2;
	String timestamp;
	String id;
	
	public String getUserid2() {
		return userid2;
	}

	public void setUserid2(String userid2) {
		this.userid2 = userid2;
	}

	public String getText() {
		return Text;
	}
	
	public void setText(String text) {
		Text = text;
	}
	
	public String getUserid() {
		return userid;
	}
	
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	public String getTimestamp() {
		return timestamp;
	}
	
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

}
