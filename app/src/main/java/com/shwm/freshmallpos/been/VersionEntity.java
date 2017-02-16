package com.shwm.freshmallpos.been;

/**
 * 版本信息
 * 
 * @author wr 2017-2-3
 */
public class VersionEntity {
	private int code = 0;
	private String name = "";
	private int opt = 0;
	private String noticeText = "";

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOpt() {
		return opt;
	}

	public void setOpt(int opt) {
		this.opt = opt;
	}

	public String getNoticeText() {
		return noticeText;
	}

	public void setNoticeText(String noticeText) {
		this.noticeText = noticeText;
	}

}
