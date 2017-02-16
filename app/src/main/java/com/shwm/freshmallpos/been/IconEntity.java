package com.shwm.freshmallpos.been;

import java.io.Serializable;

public class IconEntity implements Serializable{
	private int imgRes;
	private String text;
	
	public IconEntity(int imgRes, String text) {
		super();
		this.imgRes = imgRes;
		this.text = text;
	}
	public int getImgRes() {
		return imgRes;
	}
	public void setImgRes(int imgRes) {
		this.imgRes = imgRes;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
