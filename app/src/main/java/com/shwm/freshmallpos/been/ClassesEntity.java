package com.shwm.freshmallpos.been;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shwm.freshmallpos.util.StringUtil;

/**
 * 商品类别
 */
public class ClassesEntity implements Serializable {
	private int id;
	private String name;
//	private String tag;
	private List<ClassesEntity> listSub;
	private int lv;
	private int isLast;//1是最后一级分类
	private int supId;
	private String img;
	private boolean isFirst;// 是第一级类型吗  
	private int num;// 选中的商品数量

	private boolean selected;

	public ClassesEntity(int id, String name, boolean isFirst) {
		super();
		this.id = id;
		this.name = name;
		this.isFirst = isFirst;
		listSub = new ArrayList<ClassesEntity>();
	}

	public ClassesEntity() {
		super();
		// TODO Auto-generated constructor stub
		listSub = new ArrayList<ClassesEntity>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setId(String id) {
		this.id = StringUtil.getInt(id, -1);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSupId() {
		return supId;
	}

	public void setSupId(int supId) {
		this.supId = supId;
	}

	public void setSupId(String supId) {
		this.supId = StringUtil.getInt(supId, -1);
	}

	public int getLv() {
		return lv;
	}

	public void setLv(int lv) {
		this.lv = lv;
	}

	public void setLv(String lv) {
		this.lv = StringUtil.getInt(lv, -1);
	}

	public int getIsLast() {
		return isLast;
	}

	public void setIsLast(int isLast) {
		this.isLast = isLast;
	}

	public void setIsLast(String isLast) {
		this.isLast = StringUtil.getInt(isLast, -1);
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	// public String getTag() {
	// return tag;
	// }
	//
	// public void setTag(String tag) {
	// this.tag = tag;
	// }

	public List<ClassesEntity> getListSub() {
		return listSub;
	}

	public void setListSub(List<ClassesEntity> listSub) {
		this.listSub = listSub;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

}
