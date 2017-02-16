package com.shwm.freshmallpos.been;

import java.io.Serializable;

import com.shwm.freshmallpos.util.StringUtil;

public class ImageEntity implements Serializable {
	private String img;
	private int index;// 第几张图片
	private int typeNet;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setIndex(String index) {
		this.index = StringUtil.getInt(index, 1);
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public int getTypeNet() {
		return typeNet;
	}

	public void setTypeNet(int typeNet) {
		this.typeNet = typeNet;
	}

}
