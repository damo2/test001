package com.shwm.freshmallpos.been;

import java.io.Serializable;

import com.shwm.freshmallpos.util.StringUtil;

public class AdminEntity implements Serializable{
	private int id;
	private String username;// 账号名
	private String password;
	private String nickname;// 昵称
	private int type;// 管理员类型
	private String preName;

	private BusinessEntity business;

	public AdminEntity() {
		// TODO Auto-generated constructor stub
		business = new BusinessEntity();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setId(String id) {
		this.id = StringUtil.getInt(id);
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setType(String type) {
		this.type = StringUtil.getInt(type);
	}

	public String getPreName() {
		return preName;
	}

	public void setPreName(String preName) {
		this.preName = preName;
	}

	public BusinessEntity getBusiness() {
		return business;
	}

	public void setBusiness(BusinessEntity business) {
		this.business = business;
	}

}
