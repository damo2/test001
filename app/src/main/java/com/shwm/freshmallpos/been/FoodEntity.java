package com.shwm.freshmallpos.been;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.shwm.freshmallpos.util.StringUtil;
import com.shwm.freshmallpos.value.ValueFinal;

/**
 * 商品
 */
public class FoodEntity implements Serializable {
	private int id;
	private String name;
	private double price;
	private double priceMember;
	private String img;// Icon图片
	private String unit;// 单位
	private double num;// 购物车数量 ，购买数量
	private double numsum;// 库存
	private double numSell;// 销量
	private double priceSum;
	private int typeWeight;// 类型是否需要称重
	private String from;// 来源产地
	private String desc;// 描述
	private int statu;// 上下架状态
	private List<ImageEntity> listImg;// 图片列表
	private ClassesEntity classes; // 类型
	private String barcode;
	private String tag;
	private String priceTag;

	private boolean isNocode;// 是否是无码商品

	private int indexId;// 索引Id

	private String timeSale;
	private String typeSale;// 查日、月、年销量

	private boolean isAddstatu;

	// private boolean selected;

	public FoodEntity() {
		classes = new ClassesEntity();
		listImg = new ArrayList<ImageEntity>();
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

	public double getNumSell() {
		return numSell;
	}

	public void setNumSell(int numSell) {
		this.numSell = numSell;
	}

	public void setNumSell(String numSell) {
		this.numSell = StringUtil.getDouble(numSell);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getNumsum() {
		return numsum;
	}

	public void setNumsum(double numsum) {
		this.numsum = numsum;
	}

	public void setNumsum(int numsum) {
		this.numsum = numsum;
	}

	public void setNumsum(String numsum) {
		this.numsum = StringUtil.getDouble(numsum);
	}

	public List<ImageEntity> getListImg() {
		return listImg;
	}

	public void setListImg(List<ImageEntity> listImg) {
		this.listImg = listImg;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void setPrice(String price) {
		this.price = StringUtil.getDouble(price);
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	//
	// public boolean isSelected() {
	// return selected;
	// }
	//
	// public void setSelected(boolean selected) {
	// this.selected = selected;
	// }

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getIndexId() {
		return indexId;
	}

	public void setIndexId(int indexId) {
		this.indexId = indexId;
	}

	public ClassesEntity getClasses() {
		return classes;
	}

	public void setClasses(ClassesEntity classes) {
		this.classes = classes;
	}

	public double getPriceSum() {
		return priceSum;
	}

	public void setPriceSum(double priceSum) {
		this.priceSum = priceSum;
	}

	public int getStatu() {
		return statu;
	}

	public void setStatu(int statu) {
		this.statu = statu;
	}

	public void setStatu(String statu) {
		this.statu = StringUtil.getInt(statu);
	}

	public String getTimeSale() {
		return timeSale;
	}

	public void setTimeSale(String timeSale) {
		this.timeSale = timeSale;
	}

	public String getTypeSale() {
		return typeSale;
	}

	public void setTypeSale(String typeSale) {
		this.typeSale = typeSale;
	}

	public void setNumSell(double numSell) {
		this.numSell = numSell;
	}

	public double getPriceMember() {
		return priceMember;
	}

	public void setPriceMember(double priceMember) {
		this.priceMember = priceMember;
	}

	public void setPriceMember(String priceMember) {
		this.priceMember = StringUtil.getDouble(priceMember);
	}

	public boolean isTypeWeight() {
		return typeWeight == ValueFinal.TypeWeight_Weight;
	}

	public boolean isTypeDefault() {
		return typeWeight == ValueFinal.TypeWeight_Default;
	}

	public void setTypeWeight(int typeWeight) {
		this.typeWeight = typeWeight;
	}

	public void setTypeWeight(String typeWeight) {
		this.typeWeight = StringUtil.getInt(typeWeight);
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public double getNum() {
		return num;
	}

	public void setNum(double num) {
		this.num = num;
	}

	public void setNum(String num) {
		this.num = StringUtil.getDouble(num);
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public boolean isNocode() {
		return isNocode;
	}

	public void setNocode(boolean isNocode) {
		this.isNocode = isNocode;
	}

	public String getPriceTag() {
		return priceTag;
	}

	public void setPriceTag(String priceTag) {
		this.priceTag = priceTag;
	}

	public boolean isAddstatu() {
		return isAddstatu;
	}

	public void setAddstatu(boolean isAddstatu) {
		this.isAddstatu = isAddstatu;
	}

	public int getTypeWeight() {
		return typeWeight;
	}



}
